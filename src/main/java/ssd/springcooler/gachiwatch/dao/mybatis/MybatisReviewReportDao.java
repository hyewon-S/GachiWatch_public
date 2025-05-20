package ssd.springcooler.gachiwatch.dao.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.dao.ReviewReportDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.ReviewReportMapper;
import ssd.springcooler.gachiwatch.domain.ReviewReport;

import java.util.List;

@Repository
public class MybatisReviewReportDao implements ReviewReportDao {
    @Autowired
    private ReviewReportMapper reviewReportMapper;

    @Override
    public void createReport(ReviewReport report) {
        reviewReportMapper.createReport(report);
    }

    @Override
    public void updateReport(ReviewReport report) {
        reviewReportMapper.updateReport(report);
    }

    @Override
    public void deleteReport(int reportId) {
        reviewReportMapper.deleteReport(reportId);
    }

    @Override
    public ReviewReport getReportById(int reportId) {
        return reviewReportMapper.getReportById(reportId);
    }

    @Override
    public List<ReviewReport> getReportsByContentId(int contentId) {
        return reviewReportMapper.getReportsByContentId(contentId);
    }

    @Override
    public List<ReviewReport> getReportsByUserId(int userId) {
        return reviewReportMapper.getReportsByUserId(userId);
    }
}
