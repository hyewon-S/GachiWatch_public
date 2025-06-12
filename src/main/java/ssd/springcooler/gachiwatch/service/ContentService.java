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
import java.util.List;

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
        //DB 일단 싹다 지울거임
        contentDao.deleteAllContents();

        //movie ids 가져옴
        try {
            System.out.println("현재 API 호출 중입니다 ... 실행에는 상당 시간이 소요됩니다 ... ");
            List<Integer> movieIds = tmdbService.fetchIdsByType("movie", movie);
            List<Integer> tvIds = tmdbService.fetchIdsByType("tv", tv);
            List<Content> movieInfo = tmdbService.getDetailedInfo("movie", movieIds);
            List<Content> tvInfo = tmdbService.getDetailedInfo("tv", tvIds);


            //하나의 리스트로 합칩니다. (tv, movie가 번갈아가며 나옵니다.)
            List<Content> totalContentList = new ArrayList<Content>();
            for(int i=0; i<movie; i++) {
                    totalContentList.add(movieInfo.get(i));
                    totalContentList.add(tvInfo.get(i));
            }

            System.out.println("데이터 추출 완료! 이제 DB에 데이터를 집어넣습니다.");
            System.out.println("잠시만 기다려 주세요 ... ");
            contentDao.createContent(totalContentList);
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
            contentSummary.add(new ContentSummaryDto(cS.getContentId(), cS.getTitle(), cS.getImgUrl(), String.format("%.1f", cS.getRate())));
        }
        return contentSummary;
    }

    public ContentDto getContentDetail(int contentId) {
        Content content = contentDao.findById(contentId);
        List<String> genres = new ArrayList<>();
        for (int gId : content.getGenre()) {
            genres.add(Genre.fromGenreId(gId).getLabel());
        }

        List<String> platforms = new ArrayList<>();
        for(int pId : content.getPlatform()) {
            platforms.add(Platform.fromPlatformId(pId).getLabel());
        }
        return new ContentDto(content.getContentId(), content.getTitle(), content.getImgUrl(), String.format("%.1f", content.getRate()),
                content.getUploadDate().substring(0, 10), content.getCast(), content.getIntro(), genres, platforms);
    }
}
