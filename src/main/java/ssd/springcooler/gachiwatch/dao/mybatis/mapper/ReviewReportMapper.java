package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import ssd.springcooler.gachiwatch.domain.ReviewReport;

import java.util.List;

public interface ReviewReportMapper {

    void createReport(ReviewReport report);

    void updateReport(ReviewReport report);

    void deleteReport(int reportId);

    ReviewReport getReportById(int reportId);

    List<ReviewReport> getReportsByContentId(int contentId);

    List<ReviewReport> getReportsByUserId(int userId);
}
