package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.*;

import java.util.List;

@Mapper
public interface MemberMapper {
    // 프로필
    void updateProfile(@Param("dto") ProfileUpdateDto dto);

    List<CrewDto> selectMyCrews(int memberId);
}
