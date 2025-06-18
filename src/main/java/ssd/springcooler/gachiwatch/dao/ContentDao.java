package ssd.springcooler.gachiwatch.dao;
import ssd.springcooler.gachiwatch.domain.Content;

import java.util.List;
import java.util.Set;

public interface ContentDao {
    // 1. 콘텐츠 정보 DB에 저장
    void createContent (List<Content> content);

    void insertGenre(int contentId, List<Integer> gid);

    void insertPlatform(int contentId, List<Integer> pid);

    // 2. id 여러개로 영화/TV 시리즈의 상세정보를 가져옴
    List<Content> getContentList(List<Integer> ids);

//    // 3. 특정 검색어/제목으로 DB에서 조회
//    List<Content> findByKeyword(String keyword);

    // 4. ID로 콘텐츠 조회
    Content findById(int id);

    List<Content> getAllContentList();

    List<Integer> findAllContentIds();
}
