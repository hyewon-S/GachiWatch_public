package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssd.springcooler.gachiwatch.dao.mybatis.MybatisContentDao;
import ssd.springcooler.gachiwatch.domain.*;
import ssd.springcooler.gachiwatch.dto.ContentDto;
import ssd.springcooler.gachiwatch.dto.ContentSummaryDto;
import ssd.springcooler.gachiwatch.repository.MemberLikedContentRepository;
import ssd.springcooler.gachiwatch.repository.MemberWatchedContentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContentService {
    @Autowired
    private final TMDBService tmdbService;

    @Autowired
    private final MybatisContentDao contentDao;

    @Autowired
    private final MemberLikedContentRepository likedContentRepository;

    @Autowired
    private final MemberWatchedContentRepository watchedContentRepository;

    public ContentService(TMDBService tmdbService, MybatisContentDao contentDao, MemberLikedContentRepository likedContentRepository, MemberWatchedContentRepository watchedContentRepository) {
        this.tmdbService = tmdbService;
        this.contentDao = contentDao;
        this.likedContentRepository = likedContentRepository;
        this.watchedContentRepository = watchedContentRepository;
    }

    public void insertNewContent(int movie, int tv) {
        try {
            System.out.println("현재 API 호출 중입니다 ... 실행에는 상당 시간이 소요됩니다 ... ");
            List<Integer> movieIds = tmdbService.fetchIdsByType("movie", movie);
            List<Integer> tvIds = tmdbService.fetchIdsByType("tv", tv);

            //현재 있는 ids 가져오기
            List<Integer> contentIds = contentDao.findAllContentIds();
            Set<Integer> existingIds = new HashSet<>(contentIds);

            List<Integer> newMovieIds = new ArrayList<Integer>();
            for (int contentId : movieIds) {
                if (!existingIds.contains(contentId)) {
                    newMovieIds.add(contentId);
                }
            }
            List<Integer> newTvIds = new ArrayList<Integer>();
            for (int contentId : tvIds) {
                if (!existingIds.contains(contentId)) {
                    newTvIds.add(contentId);
                }
            }

            List<Content> movieInfo = tmdbService.getDetailedInfo("movie", newMovieIds);
            List<Content> tvInfo = tmdbService.getDetailedInfo("tv", newTvIds);

            System.out.println("데이터 추출 완료! 이제 DB에 데이터를 집어넣습니다.");
            System.out.println("잠시만 기다려 주세요 ... ");
            contentDao.createContent(movieInfo);
            contentDao.createContent(tvInfo);
            System.out.println("DB 저장 완료!! 결과 확인!");
        } catch(Exception e) {
            System.out.println("movie 정보를 API에서 가져오던 중 예외 발생!");
            e.printStackTrace();
        }
    }

    public List<ContentSummaryDto> getContentSummary() {

        List<Content> contentList = contentDao.getAllContentList();
        List<ContentSummaryDto> contentSummary = new ArrayList<ContentSummaryDto>();
        for(Content cS : contentList) {
            List<String> ott = new ArrayList<>();
            for(Integer pId : cS.getPlatform()) {
                ott.add(Platform.fromPlatformId(pId).name());
            }
            contentSummary.add(new ContentSummaryDto(cS.getContentId(), cS.getTitle(), cS.getImgUrl(), String.format("%.1f", cS.getRate()), ott, cS.getContentType()));
        }
        return contentSummary;
    }

    public ContentDto getContentDetail(int contentId) {
        Content content = contentDao.findById(contentId);
        if(content != null) {
            List<String> genres = new ArrayList<>();
            for (int gId : content.getGenre()) {
                genres.add(Genre.fromGenreId(gId).getLabel());
            }

            List<String> platforms = new ArrayList<>();
            for (int pId : content.getPlatform()) {
                platforms.add(Platform.fromPlatformId(pId).getLabel());
            }
            return new ContentDto(content.getContentId(), content.getTitle(), content.getImgUrl(), String.format("%.1f", content.getRate()),
                    content.getUploadDate().substring(0, 10), content.getCast(), content.getIntro(), genres, platforms);
        }
        else {
            return null;
        }
    }

    public ContentDto getContentInfo(int contentId) throws Exception {
        List<Content> oneContent = tmdbService.getDetailedInfo("movie", List.of(contentId));
        contentDao.createContent(oneContent);
        List<String> genres = new ArrayList<>();
        for (int gId : oneContent.get(0).getGenre()) {
            genres.add(Genre.fromGenreId(gId).getLabel());
        }
        List<String> platforms = new ArrayList<>();
        for(int pId : oneContent.get(0).getPlatform()) {
            platforms.add(Platform.fromPlatformId(pId).getLabel());
            System.out.println(oneContent.get(0).getPlatform());
        }
        return new ContentDto(oneContent.get(0).getContentId(), oneContent.get(0).getTitle(), oneContent.get(0).getImgUrl(), String.format("%.1f", oneContent.get(0).getRate()),
                oneContent.get(0).getUploadDate().substring(0, 10), oneContent.get(0).getCast(), oneContent.get(0).getIntro(), genres, platforms);
    }

    public String checkHeart(int contentId, int memberId) {
        String likedUrl = "/image/icon/icon-heart-black.png";
        boolean isLiked = likedContentRepository.existsByMemberIdAndContentId(memberId, contentId);
        if(isLiked) {
            likedUrl = "/image/icon/icon-heart-red.png";
        }
        return likedUrl;
    }

    public String checkWatched(int contentId, int memberId) {
        String watchedUrl = "/image/icon/icon-hidden.png";
        boolean isWatched = watchedContentRepository.existsByMemberIdAndContentId(memberId, contentId);
        if(isWatched) {
            watchedUrl = "/image/icon/icon-eye.png";
        }
        return watchedUrl;
    }

    @Transactional
    public void updateLiked(int contentId, int memberId, boolean isLiked) {
        if(isLiked) {
            LikedContent liked = new LikedContent(memberId, contentId);
            likedContentRepository.save(liked);
        } else {
            likedContentRepository.deleteByMemberIdAndContentId(memberId, contentId);
        }
    }

    @Transactional
    public void updateWatched(int contentId, int memberId, boolean isWatched) {
        if(isWatched) {
            WatchedContent watched = new WatchedContent(memberId, contentId);
            watchedContentRepository.save(watched);
        } else {
            watchedContentRepository.deleteByMemberIdAndContentId(memberId, contentId);
        }
    }

    // 추천 결과 (contentId + score)를 받아서 ContentDto 리스트로 변환하는 함수
    public List<ContentDto> getContentsByRecommendation(List<Map<String, Object>> recommendList) {
        if (recommendList == null || recommendList.isEmpty()) {
            return Collections.emptyList();
        }

        List<ContentDto> result = recommendList.stream()
                .map(map -> {
                    // content_id를 Integer로 안전하게 파싱
                    Object contentIdObj = map.get("content_id");
                    if (contentIdObj == null) return null;

                    int contentId;
                    if (contentIdObj instanceof Integer) {
                        contentId = (Integer) contentIdObj;
                    } else if (contentIdObj instanceof Number) {
                        contentId = ((Number) contentIdObj).intValue();
                    } else {
                        try {
                            contentId = Integer.parseInt(contentIdObj.toString());
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }

                    // contentId로 Content 엔티티 조회
                    Content content = contentDao.findById(contentId);
                    if (content == null) return null;

                    // Content -> ContentDto 변환 (기존 getContentDetail 메서드와 유사)
                    List<String> genres = new ArrayList<>();
                    for (int gId : content.getGenre()) {
                        genres.add(Genre.fromGenreId(gId).getLabel());
                    }
                    List<String> platforms = new ArrayList<>();
                    for (int pId : content.getPlatform()) {
                        platforms.add(Platform.fromPlatformId(pId).getLabel());
                    }
                    return new ContentDto(content.getContentId(),
                            content.getTitle(),
                            content.getImgUrl(),
                            String.format("%.1f", content.getRate()),
                            content.getUploadDate().substring(0, 10),
                            content.getCast(),
                            content.getIntro(),
                            genres,
                            platforms);
                })
                .filter(dto -> dto != null) // null 필터링
                .collect(Collectors.toList());

        return result;
    }
}
