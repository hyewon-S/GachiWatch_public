package ssd.springcooler.gachiwatch.dao.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.springframework.web.multipart.MultipartFile;
import ssd.springcooler.gachiwatch.dao.MemberDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.MemberMapper;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.*;

@Repository
public class MyBatisMemberDao implements MemberDao {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member findByEmailAndPassword(LoginDto loginDto) {
        return memberMapper.findByEmailAndPassword(loginDto);
    }

    @Override
    public void updateProfile(ProfileUpdateDto dto) {
        memberMapper.updateProfile(dto);
    }

    @Override
    public List<CrewDto> selectMyCrews(int memberId) {
        return memberMapper.selectMyCrews(memberId);
    }
}

