package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import ssd.springcooler.gachiwatch.domain.ReviewReport;

import java.util.List;

@Mapper
public interface ReviewReportMapper {

    void createReport(ReviewReport report);

    void updateReport(ReviewReport report);

    void deleteReport(int reportId);

    ReviewReport getReportById(int reportId);

    List<ReviewReport> getReportsByUserId(int userId);
}
