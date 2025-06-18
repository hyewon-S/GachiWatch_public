package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.dao.mybatis.MybatisContentDao;
import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.ContentDto;
import ssd.springcooler.gachiwatch.dto.ContentSummaryDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ContentService {
    @Autowired
    private final TMDBService tmdbService;

    @Autowired
    private final MybatisContentDao contentDao;

    public ContentService(TMDBService tmdbService, MybatisContentDao contentDao) {
        this.tmdbService = tmdbService;
        this.contentDao = contentDao;
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
}
