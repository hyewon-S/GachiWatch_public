package ssd.springcooler.gachiwatch.dto;

import lombok.*;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.CrewChat;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CrewDto {
    private Optional<Crew> crew;

    private int crewId;
    private String crewName;
    private String crewDesc;
    private Platform platform;
    private int payment;
    private Date payDate;
    private int maxMember;
    private String account;
    private int currentPeople;

    private Long captainId; // captain의 id만 전달
    private List<Long> memberIds; // crew에 속한 member들의 id 리스트
    private List<CrewChat> chatList;

    public CrewDto(Crew crew, List<CrewChat> chatList) {
        this.crew = Optional.ofNullable(crew);
        this.chatList = new ArrayList<>(chatList); // Hibernate PersistentBag -> ArrayList로 안전하게 변환
    }

    public CrewDto(Crew crew, List<CrewChat> chatList, int maxMember) {
        this.crew = Optional.ofNullable((crew));
        this.chatList = chatList;
        this.currentPeople = maxMember;
    }

    public CrewDto(Optional<Crew> crew, List<CrewChat> chatList) {
        this.crew = crew;
        this.chatList = new ArrayList<>(chatList);
    }
    /*
    public CrewDto(Crew crew, List<CrewChat> chatList) {
        this.crew = crew;
        this.chatList = chatList;
    }
    */

    public Object getCrew() {
        return crew;
    }

    public List<CrewChat> getChatList() {
        return chatList;
    }
}
