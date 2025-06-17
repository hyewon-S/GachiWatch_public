package ssd.springcooler.gachiwatch.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import okhttp3 .OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.*;
import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.dto.ForMeContentDto;
import ssd.springcooler.gachiwatch.dto.LatestMovieDto;
import ssd.springcooler.gachiwatch.dto.TrendingContentDto;

@Service
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
                    int genre_id = genresJson.getJSONObject(i).optInt("id");
                    genres.add(mappingGenreId(genre_id));
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
            else {
                platforms.add(0);
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

    private int mappingGenreId(int genreId) {
        /*
        Talk(10767) -> 가족
        News(10763) -> 다큐멘터리
        Reality(10764) -> 다큐멘터리
        Soap(10766) -> 드라마
        Kids(10762) -> 애니메이션
        Action&Adventure(10759) -> 액션, 모험
        War&Politics(10768) -> 전쟁
        Sci-Fi&Fantasy(10765) -> 판타지, SF
        */

        Genre g;
        g = switch (genreId) {
            case 10767 -> Genre.fromDisplayName("가족");
            case 10763, 10764 -> Genre.fromDisplayName("다큐멘터리");
            case 10766 -> Genre.fromDisplayName("드라마");
            case 10762 -> Genre.fromDisplayName("애니메이션");
            case 10759 -> Genre.fromDisplayName("액션");
            case 10768 -> Genre.fromDisplayName("전쟁");
            case 10765 -> Genre.fromDisplayName("판타지");
            default -> Genre.fromGenreId(genreId); //널 방지..
        };
        return g.getGenre_id();
    }

    //비회원 메인페이지 "실시간 트렌드" 관련 코드
    private static final String TMDB_TRENDING_URL = "https://api.themoviedb.org/3/trending/all/week?language=ko-KR&api_key=" + API_KEY;

    public List<TrendingContentDto> getTrendingContents(int count) throws Exception {
        List<TrendingContentDto> contents = new ArrayList<>();
        String jsonResponse = readUrl(TMDB_TRENDING_URL);

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray results = jsonObject.getJSONArray("results");

        for (int i = 0; i < results.length() && contents.size() < count; i++) {
            JSONObject obj = results.getJSONObject(i);
            int id = obj.getInt("id");
            String posterPath = obj.optString("poster_path", null);
            String mediaType = obj.optString("media_type", "movie");

            if (posterPath != null && !posterPath.isEmpty()) {
                contents.add(new TrendingContentDto(id, posterPath, mediaType));
            }
        }

        return contents;
    }

    // 비회원 메인페이지 "최신 영화" 관련 코드
    public List<LatestMovieDto> getLatestMovies(int count) throws Exception {
        List<LatestMovieDto> latestMovies = new ArrayList<>();

        // 오늘 날짜 문자열 생성 (yyyy-MM-dd 포맷)
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        // discover API 최신 영화 20개, release_date <= today 조건 추가
        String url = String.format(
                "https://api.themoviedb.org/3/discover/movie?api_key=%s&language=ko-KR&sort_by=release_date.desc&region=KR&release_date.lte=%s&page=1",
                API_KEY, today);

        JSONObject response = new JSONObject(readUrl(url));
        JSONArray results = response.getJSONArray("results");

        for (int i = 0; i < results.length() && latestMovies.size() < count; i++) {
            JSONObject movie = results.getJSONObject(i);
            int id = movie.getInt("id");
            String posterPath = movie.optString("poster_path", null);
            if (posterPath != null && !posterPath.isEmpty()) {
                latestMovies.add(new LatestMovieDto(id, posterPath));
            }
        }

        return latestMovies;
    }

    //회원 메인페이지 "나를 위한 추천" 관련 코드 (사용자가 선택한 선호 장르 기반 추천)

    public List<ForMeContentDto> getForMeContents(int count, List<Genre> genreIdList) throws Exception {
        List<ForMeContentDto> contents = new ArrayList<>();

        // 장르 리스트 -> 쉼표 구분된 genre_id 문자열로 변환
        String genreIdParam = genreIdList.stream()
                .map(g -> String.valueOf(g.getGenre_id()))
                .collect(Collectors.joining(","));

        // genreIdParam이 비어있을 경우 기본 장르 ID 사용 (예: 액션 28) 비어있을 경우 액션을 default로 보여줌
        if (genreIdParam.isEmpty()) {
            genreIdParam = "28";
        }

        // 영화 API URL
        final String MOVIE_URL = "https://api.themoviedb.org/3/discover/movie"
                + "?with_genres=" + genreIdParam
                + "&language=ko-KR"
                + "&sort_by=popularity.desc"
                + "&region=KR"
                + "&api_key=" + API_KEY;

        // TV API URL
        final String TV_URL = "https://api.themoviedb.org/3/discover/tv"
                + "?with_genres=" + genreIdParam
                + "&language=ko-KR"
                + "&sort_by=popularity.desc"
                + "&region=KR"
                + "&api_key=" + API_KEY;

        // 영화 콘텐츠 먼저 가져오기
        contents.addAll(fetchContentFromUrl(MOVIE_URL, "movie", count));

        // TV 콘텐츠는 남은 수만큼 추가
        if (contents.size() < count) {
            int remaining = count - contents.size();
            contents.addAll(fetchContentFromUrl(TV_URL, "tv", remaining));
        }

        return contents;
    }

    private List<ForMeContentDto> fetchContentFromUrl(String url, String mediaType, int maxCount) throws Exception {
        List<ForMeContentDto> result = new ArrayList<>();
        int page = 1;

        while (result.size() < maxCount) {
            String pagedUrl = url + "&page=" + page;
            String jsonResponse = readUrl(pagedUrl);

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray("results");

            if (results.length() == 0) break; // 더 이상 결과 없음

            for (int i = 0; i < results.length() && result.size() < maxCount; i++) {
                JSONObject obj = results.getJSONObject(i);
                int id = obj.getInt("id");
                String posterPath = obj.optString("poster_path", null);

                if (posterPath != null && !posterPath.isEmpty()) {
                    result.add(new ForMeContentDto(id, posterPath, mediaType));
                }
            }

            page++;
        }

        return result;
    }
}
