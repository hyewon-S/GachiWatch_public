package ssd.springcooler.gachiwatch.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.*;

public interface MemberDao {
    // 로그인
    Member findByEmailAndPassword(LoginDto loginDto);

    // 프로필
    void updateProfile(ProfileUpdateDto dto);

    // 참여한 가치크루
    List<CrewDto> selectMyCrews(int memberId);
}
