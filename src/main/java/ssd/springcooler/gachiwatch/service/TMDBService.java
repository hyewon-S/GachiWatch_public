package ssd.springcooler.gachiwatch.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import ssd.springcooler.gachiwatch.dto.*;

@Service
public class TMDBService {
    private static final String API_KEY = "4ff777b6f68301ca1ed6a38a6d157461";

    private static final OkHttpClient client = new OkHttpClient();
    private static final Set<Integer> PLATFORM_PROVIDER_IDS = Set.of(8, 97, 337, 350, 356, 119); // 넷플릭스, 왓챠, 디즈니+, 애플 tv+, 웨이브, 아마존
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

    private int mappingGenreId(int genreId) {Genre g;
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

    //키워드로 키워드 ID 검색
    public int getKeywordId(String query, String apiKey) throws Exception {
        String urlStr = String.format(
                "https://api.themoviedb.org/3/search/keyword?api_key=%s&query=%s",
                apiKey, URLEncoder.encode(query, "UTF-8")
        );

        String response = readUrlForKeyword(urlStr);
        JSONObject json = new JSONObject(response);
        JSONArray results = json.getJSONArray("results");

        if (results.length() > 0) {
            return results.getJSONObject(0).getInt("id"); // 첫 번째 키워드 ID 반환
        } else {
            throw new Exception("Keyword not found");
        }
    }

    //키워드 ID로 영화 검색
    public JSONArray getMoviesByKeyword(int keywordId, String apiKey) throws Exception {
        String urlStr = String.format(
                "https://api.themoviedb.org/3/keyword/%d/movies?api_key=%s&language=ko-KR",
                keywordId, apiKey
        );

        String response = readUrlForKeyword(urlStr);
        JSONObject json = new JSONObject(response);
        return json.getJSONArray("results");
    }

    //키워드 ID로 tv 검색
    public JSONArray getTvByKeyword(int keywordId, String apiKey) throws Exception {
        String urlStr = String.format(
                "https://api.themoviedb.org/3/discover/tv?api_key=%s&with_keywords=%d&language=ko-KR",
                apiKey, keywordId
        );

        String response = readUrlForKeyword(urlStr);
        JSONObject json = new JSONObject(response);
        return json.getJSONArray("results");
    }

    public String readUrlForKeyword(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();
        return content.toString();
    }

    public List<ContentSummaryDto> runKeywordSearch(String keyword) {
        try {
            int keywordId = getKeywordId(keyword, API_KEY);

            JSONArray movieResults = getMoviesByKeyword(keywordId, API_KEY);
            JSONArray tvResults = getTvByKeyword(keywordId, API_KEY);
            List<ContentSummaryDto> contentList = new ArrayList<>();

            for (int i = 0; i < movieResults.length(); i++) {
                JSONObject movie = movieResults.getJSONObject(i);
                ContentSummaryDto content = new ContentSummaryDto();
                content.setContentId((int)movie.get("id"));
                content.setContentType("movie");
                content.setTitle(movie.getString("title"));
                content.setThumbnailUrl("https://image.tmdb.org/t/p/w500" + movie.getString("poster_path"));
                BigDecimal voteAverage = (BigDecimal) movie.get("vote_average");
                content.setRating(String.format("%.1f", voteAverage.doubleValue()));
                contentList.add(content);
            }

            for (int i = 0; i < tvResults.length(); i++) {
                JSONObject tv = tvResults.getJSONObject(i);
                ContentSummaryDto content = new ContentSummaryDto();
                content.setContentId((int)tv.get("id"));
                content.setTitle(tv.getString("name"));
                content.setContentType("tv");
                content.setThumbnailUrl("https://image.tmdb.org/t/p/w500" + tv.getString("poster_path"));
                BigDecimal voteAverage = (BigDecimal) tv.get("vote_average");
                content.setRating(String.format("%.1f", voteAverage.doubleValue()));

                contentList.add(content);
            }

            return contentList;
        } catch (Exception e) {
            System.out.println("❌ 키워드 검색 실패: 한글일 수 있음");
            //한글에 대한 처리
            e.printStackTrace();
        }
        return null;
    }



    //notification 관련 코드
//    public EmailNotiDto getTopPopularContentByGenres(List<String> genreNames) throws Exception {
//        // 1. genreNames -> genreIds
//        List<Integer> genreIds = new ArrayList<>();
//        for (String name : genreNames) {
//            Genre g = Genre.fromDisplayName(name);
//            if (g != null) {
//                genreIds.add(g.getGenre_id());
//            }
//        }
//
//        if (genreIds.isEmpty()) {
//            return null; // 추천할 장르가 없음
//        }
//
//        // 2. 장르 기반 discover API 요청
//        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
//        String genreParam = genreIds.stream().map(String::valueOf).collect(Collectors.joining(","));
//
//        String url = String.format(
//                "https://api.themoviedb.org/3/discover/movie?api_key=%s&language=ko-KR&sort_by=popularity.desc&region=KR&release_date.lte=%s&with_genres=%s",
//                API_KEY, today, genreParam
//        );
//
//        JSONObject response = new JSONObject(readUrl(url));
//        JSONArray results = response.optJSONArray("results");
//
//        if (results == null || results.length() == 0) {
//            return null; // 결과 없음
//        }
//
//        JSONObject topMovie = results.getJSONObject(0);
//        String title = topMovie.optString("title", "제목 없음");
//        int contentId = topMovie.getInt("id");
//        JSONArray resultGenres = topMovie.optJSONArray("genre_ids");
//        List<Integer> resultGenreIds = new ArrayList<>();
//        if (resultGenres != null) {
//            for (int i = 0; i < resultGenres.length(); i++) {
//                resultGenreIds.add(resultGenres.getInt(i));
//            }
//        }
//
//        // 3. 일치하는 장르명 찾기
//        String matchedGenreName = "추천 장르";
//        for (String name : genreNames) {
//            Genre g = Genre.fromDisplayName(name);
//            if (g != null && resultGenreIds.contains(g.getGenre_id())) {
//                matchedGenreName = name;
//                break;
//            }
//        }
//
//        // 4. OTT 플랫폼 조회
//        String platformName = "해당 OTT";
//        String platformUrl = String.format(
//                "https://api.themoviedb.org/3/movie/%d/watch/providers?api_key=%s", contentId, API_KEY);
//        JSONObject providerResponse = new JSONObject(readUrl(platformUrl));
//        JSONObject kr = providerResponse.optJSONObject("results") != null
//                ? providerResponse.getJSONObject("results").optJSONObject("KR") : null;
//        JSONArray flatrate = kr != null ? kr.optJSONArray("flatrate") : null;
//
//        if (flatrate != null && flatrate.length() > 0) {
//            for (int i = 0; i < flatrate.length(); i++) {
//                int pid = flatrate.getJSONObject(i).optInt("provider_id");
//                if (PROVIDER_ID_TO_PLATFORM.containsKey(pid)) {
//                    platformName = PROVIDER_ID_TO_PLATFORM.get(pid);
//                    break;
//                }
//            }
//        }
//
//        return new EmailNotiDto(title, matchedGenreName, platformName);
//    }
//    public EmailNotiDto getTopPopularContentByGenres(List<Genre> genreNames) throws Exception {
//        // 1. genreNames -> genreIds
//        List<Integer> genreIds = new ArrayList<>();
//        for (Genre name : genreNames) {
//            Genre g = Genre.fromDisplayName(String.valueOf(name));
//            if (g != null) {
//                genreIds.add(g.getGenre_id());
//            }
//        }
//
//        if (genreIds.isEmpty()) {
//            return null; // 추천할 장르가 없음
//        }
//
//        // 2. 장르 기반 discover API 요청
//        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
//        String genreParam = genreIds.stream().map(String::valueOf).collect(Collectors.joining(","));
//
//        String url = String.format(
//                "https://api.themoviedb.org/3/discover/movie?api_key=%s&language=ko-KR&sort_by=popularity.desc&region=KR&release_date.lte=%s&with_genres=%s",
//                API_KEY, today, genreParam
//        );
//
//        JSONObject response = new JSONObject(readUrl(url));
//        JSONArray results = response.optJSONArray("results");
//
//        if (results == null || results.length() == 0) {
//            return null; // 결과 없음
//        }
//
//        // 3. 인기 콘텐츠 순회하며 OTT 정보 있는 첫 콘텐츠 찾기
//        for (int idx = 0; idx < results.length(); idx++) {
//            JSONObject movie = results.getJSONObject(idx);
//            String title = movie.optString("title", "제목 없음");
//            int contentId = movie.getInt("id");
//
//            // 장르 매칭
//            JSONArray resultGenres = movie.optJSONArray("genre_ids");
//            List<Integer> resultGenreIds = new ArrayList<>();
//            if (resultGenres != null) {
//                for (int i = 0; i < resultGenres.length(); i++) {
//                    resultGenreIds.add(resultGenres.getInt(i));
//                }
//            }
//
//            String matchedGenreName = "추천 장르";
//            for (Genre name : genreNames) {
//                Genre g = Genre.fromDisplayName(String.valueOf(name));
//                if (g != null && resultGenreIds.contains(g.getGenre_id())) {
//                    matchedGenreName = String.valueOf(name);
//                    break;
//                }
//            }
//
//            // OTT 플랫폼 조회
//            String platformUrl = String.format(
//                    "https://api.themoviedb.org/3/movie/%d/watch/providers?api_key=%s", contentId, API_KEY);
//            JSONObject providerResponse = new JSONObject(readUrl(platformUrl));
//            JSONObject kr = providerResponse.optJSONObject("results") != null
//                    ? providerResponse.getJSONObject("results").optJSONObject("KR") : null;
//            JSONArray flatrate = kr != null ? kr.optJSONArray("flatrate") : null;
//
//            if (flatrate != null && flatrate.length() > 0) {
//                for (int i = 0; i < flatrate.length(); i++) {
//                    int pid = flatrate.getJSONObject(i).optInt("provider_id");
//                    if (PROVIDER_ID_TO_PLATFORM.containsKey(pid)) {
//                        String platformName = PROVIDER_ID_TO_PLATFORM.get(pid);
//                        return new EmailNotiDto(title, matchedGenreName, platformName);
//                    }
//                }
//            }
//            // OTT가 없다면 다음 콘텐츠로 넘어감
//        }
//
//        // 모든 콘텐츠에 OTT 제공 정보가 없으면 null 반환
//        return null;
//    }

    public EmailNotiDto getTopPopularContentByGenres(List<Genre> genreList) throws Exception {
        // 1. genre 객체 리스트 -> genre ID 리스트
        List<Integer> genreIds = genreList.stream()
                .map(Genre::getGenre_id)
                .collect(Collectors.toList());

        if (genreIds.isEmpty()) return null;

        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String genreParam = genreIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        // 2. discover API 호출 (인기순 정렬)
        String url = String.format(
                "https://api.themoviedb.org/3/discover/movie?api_key=%s&language=ko-KR&sort_by=popularity.desc&region=KR&release_date.lte=%s&with_genres=%s",
                API_KEY, today, genreParam
        );

        JSONObject response = new JSONObject(readUrl(url));
        JSONArray results = response.optJSONArray("results");

        if (results == null || results.length() == 0) {
            return null;
        }

        JSONObject topMovie = results.getJSONObject(0);
        String title = topMovie.optString("title", "제목 없음");
        int contentId = topMovie.getInt("id");

        // 3. 장르 정보 추출
        JSONArray genreIdArray = topMovie.optJSONArray("genre_ids");
        String matchedGenreName = "추천 장르";
        if (genreIdArray != null) {
            for (int i = 0; i < genreIdArray.length(); i++) {
                int id = genreIdArray.getInt(i);
                for (Genre g : genreList) {
                    if (g.getGenre_id() == id) {
                        matchedGenreName = g.getLabel(); // Genre enum에 getDisplayName() 있어야 함
                        break;
                    }
                }
            }
        }

        // 4. 플랫폼 정보 추출
        String platformUrl = String.format(
                "https://api.themoviedb.org/3/movie/%d/watch/providers?api_key=%s", contentId, API_KEY);
        JSONObject providerResponse = new JSONObject(readUrl(platformUrl));
        JSONObject kr = providerResponse.optJSONObject("results") != null
                ? providerResponse.getJSONObject("results").optJSONObject("KR")
                : null;
        JSONArray flatrate = kr != null ? kr.optJSONArray("flatrate") : null;

        String platformName = "해당 없음";
        if (flatrate != null) {
            for (int i = 0; i < flatrate.length(); i++) {
                int pid = flatrate.getJSONObject(i).getInt("provider_id");
                if (PROVIDER_ID_TO_PLATFORM.containsKey(pid)) {
                    platformName = PROVIDER_ID_TO_PLATFORM.get(pid);
                    break;
                }
            }
        }

        return new EmailNotiDto(title, matchedGenreName, platformName);
    }

}
