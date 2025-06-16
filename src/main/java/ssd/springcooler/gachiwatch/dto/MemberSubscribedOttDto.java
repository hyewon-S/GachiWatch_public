package ssd.springcooler.gachiwatch.dto;

import lombok.Data;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.List;

@Data
public class MemberSubscribedOttDto {
    private List<Platform> ottList;  // 사용자가 선택한 ott 목록
}