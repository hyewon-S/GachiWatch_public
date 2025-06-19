package ssd.springcooler.gachiwatch.dao.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.dao.ContentDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.ContentMapper;
import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.dto.ContentSummaryDto;

import java.util.List;
import java.util.Set;

@Repository
public class MybatisContentDao implements ContentDao {
    @Autowired
    private ContentMapper contentMapper;

    @Override
    public void createContent (List<Content> content) {
        for (Content c : content) {
            contentMapper.createContent(c);
            int content_id = c.getContentId();
            insertGenre(content_id, c.getGenre());
            insertPlatform(content_id, c.getPlatform());
        }
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
    @Override
    public List<Content> getAllContentList() {
        List<Content> contentList = contentMapper.getAllContentList();
        for (Content content : contentList) {
            List<Integer> genres = contentMapper.getGenresByContentId(content.getContentId());
            List<Integer> platforms = contentMapper.getPlatformsByContentId(content.getContentId());
            content.setGenre(genres);
            content.setPlatform(platforms);
        }
        return contentList;
    }
    @Override
    public Content findById(int id) {
        Content content = contentMapper.findById(id);
        if(content != null) {
            List<Integer> genres = contentMapper.getGenresByContentId(id);
            List<Integer> platforms = contentMapper.getPlatformsByContentId(id);
            content.setGenre(genres);
            content.setPlatform(platforms);
        }
        return content;
    }
    @Override
    public List<Integer> findAllContentIds() {
        return contentMapper.findAllContentIds();
    }

    @Override
    public List<ContentSummaryDto> searchByKeyword(String keyword) {
        return contentMapper.searchByKeyword(keyword);
    }
}
