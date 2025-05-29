package ssd.springcooler.gachiwatch.dao.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.dao.ContentDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.ContentMapper;
import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.List;

@Repository
public class MybatisContentDao implements ContentDao {
    @Autowired
    private ContentMapper contentMapper;

    @Override
    public void createContent (Content content) {
        contentMapper.createContent(content);
        //여기에 장르랑 플랫폼 insert 차후에 합치기!!
    }

    @Override
    public void insertGenre (int contentId, List<Integer> gid) {
        for (int genreId : gid) {
            contentMapper.insertGenre(contentId, genreId);
        }
    }

    @Override
    public void insertPlatform (int contentId, List<Integer> pid) {
        for (int platformId : pid) {
            contentMapper.insertPlatform(contentId, platformId);
        }
    }

    //장르랑 플랫폼은 따로 가져와서 주입
    @Override
    public List<Content> getContentList(List<Integer> ids) {
        List<Content> contentList = contentMapper.getContentList(ids);
        for (Content content : contentList) {
            List<Integer> genres = contentMapper.getGenresByContentId(content.getContentId());
            List<Integer> platforms = contentMapper.getPlatformsByContentId(content.getContentId());
            content.setGenre(genres);
            content.setPlatform(platforms);
        }
        return contentList;
    }

//    @Override 나중에 검색 기능 쓰게되면..
//    public List<Content> findByKeyword(String keyword) {
//        return contentMapper.findByKeyword(keyword);
//    }

    @Override
    public Content findById(int id) {
        Content content = contentMapper.findById(id);
        List<Integer> genres = contentMapper.getGenresByContentId(content.getContentId());
        List<Integer> platforms = contentMapper.getPlatformsByContentId(content.getContentId());
        content.setGenre(genres);
        content.setPlatform(platforms);
        return content;
    }

    @Override
    public void updateContent(Content content) {
        contentMapper.updateContent(content);
    }

    @Override
    public void deleteContent(int id) {
        contentMapper.deleteContent(id);
    }

    @Override
    public List<Content> search(List<Integer> genre, List<Integer> platform, String content_type, String range) {
        return contentMapper.search(genre, platform, content_type, range);
    }
}
