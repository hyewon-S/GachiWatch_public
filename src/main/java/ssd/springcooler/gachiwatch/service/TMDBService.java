package ssd.springcooler.gachiwatch.service;

import java.util.*;

import okhttp3 .OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.*;
import ssd.springcooler.gachiwatch.domain.Content;

public class TMDBService {
    private static final String API_KEY = "4ff777b6f68301ca1ed6a38a6d157461";
    private static final String REGION = "KR";

    private static final OkHttpClient client = new OkHttpClient();
    private static final Set<Integer> PLATFORM_PROVIDER_IDS = Set.of(8, 97, 337, 350, 356, 119); // 넷플릭스, 왓챠, 디즈니+, 애플 tv+, 웨이브, 아마존
//    private static final Set<Integer> PLATFORM_PROVIDER_IDS = Set.of(1881);
    private static final Map<Integer, String> PROVIDER_ID_TO_PLATFORM = Map.of(
            8, "NETFLIX",
            97, "WATCHA",
            337, "DISNEY",
            350, "Apple TV+",
            356, "WAVVE",
            119, "AMAZON"
    );

    public List<Integer> fetchIdsByType(String type, int count) throws Exception {
        Set<Integer> idSet = new LinkedHashSet<>();

        int page = 1;
        while (idSet.size() < count && page <= 10) {  // page 제한 (TMDB는 500까지 지원)
            String url = String.format("https://api.themoviedb.org/3/trending/%s/week?api_key=%s&page=%d", type, API_KEY, page);
            JSONObject response = new JSONObject(readUrl(url));
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length() && idSet.size() < count; i++) {
                int id = results.getJSONObject(i).getInt("id");
                if (hasValidPlatform(type, id)) {
                    idSet.add(id);
                }
            }
            page++;
        }
        return new ArrayList<>(idSet);
    }

    private boolean hasValidPlatform(String type, int id) throws Exception {
        String url = String.format("https://api.themoviedb.org/3/%s/%d/watch/providers?api_key=%s", type, id, API_KEY);
        JSONObject response = new JSONObject(readUrl(url));
        JSONObject kr = response.optJSONObject("results") != null
                ? response.getJSONObject("results").optJSONObject("KR") : null;
        JSONArray flatrate = kr != null ? kr.optJSONArray("flatrate") : null;

        if (flatrate != null) {
            for (int i = 0; i < flatrate.length(); i++) {
                int pid = flatrate.getJSONObject(i).getInt("provider_id");
                if (PLATFORM_PROVIDER_IDS.contains(pid)) return true;
            }
        }
        return false;
    }

    public List<Content> getDetailedInfo(String type, List<Integer> ids) throws Exception {
        List<Content> list = new ArrayList<>();

        for (int id : ids) {
            String detailUrl = String.format("https://api.themoviedb.org/3/%s/%d?api_key=%s&language=ko-KR", type, id, API_KEY);

            JSONObject detail = new JSONObject(readUrl(detailUrl));

            String title = detail.optString(type.equals("movie") ? "title" : "name");
            String overview = detail.optString("overview");
            String releaseDate = detail.optString(type.equals("movie") ? "release_date" : "first_air_date");
            double rating = detail.optDouble("vote_average");
            String imageUrl = "https://image.tmdb.org/t/p/w500" + detail.optString("poster_path");

            JSONArray genresJson = detail.optJSONArray("genres");
            List<Integer> genres = new ArrayList<>();
            if (genresJson != null) {
                for (int i = 0; i < genresJson.length(); i++) {
                    //genres.add(genresJson.getJSONObject(i).optString("name"));
                    genres.add(genresJson.getJSONObject(i).optInt("id"));
                }
            }

            // 출연진
            String creditUrl = String.format("https://api.themoviedb.org/3/%s/%d/credits?api_key=%s", type, id, API_KEY);
            JSONObject creditJson = new JSONObject(readUrl(creditUrl));
            JSONArray castArray = creditJson.optJSONArray("cast");
            StringBuilder cast = new StringBuilder();
            if (castArray != null) {
                for (int i = 0; i < Math.min(5, castArray.length()); i++) {
                    if (i > 0) cast.append(", ");
                    cast.append(castArray.getJSONObject(i).optString("name"));
                }
            }

            // 플랫폼
            String platformUrl = String.format("https://api.themoviedb.org/3/%s/%d/watch/providers?api_key=%s", type, id, API_KEY);
            JSONObject platformResponse = new JSONObject(readUrl(platformUrl));
            JSONObject kr = platformResponse.optJSONObject("results") != null ? platformResponse.getJSONObject("results").optJSONObject("KR") : null;
            JSONArray flatrate = kr != null ? kr.optJSONArray("flatrate") : null;
            List<Integer> platforms = new ArrayList<>();
            if (flatrate != null) {
                for (int i = 0; i < flatrate.length(); i++) {
                    int pid = flatrate.getJSONObject(i).getInt("provider_id");
                    if (PROVIDER_ID_TO_PLATFORM.containsKey(pid)) {
                        platforms.add(pid);
                    }
                }
            }

            Content c = new Content(id, title, overview, genres, cast.toString(), platforms, rating, releaseDate, imageUrl, type);
            list.add(c);
        }

        return list;
    }

    private String readUrl(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
