package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CREWCHAT")
public class CrewChat {
//    @EmbeddedId
//    private CrewChatId id;
//
//    String chat;
//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    Member member;
//
//    public CrewChat(String chat) {
//        this.chat = chat;
//    }
//
//    public CrewChat(Crew crew, Date date, String chat, Member member) {
//        this.id = new CrewChatId(crew, date);
//        this.chat = chat;
//        this.member = member;
//    }
@EmbeddedId
private CrewChatId id;

    @ManyToOne
    @MapsId("crewId") // EmbeddedId의 필드 이름과 매핑
    @JoinColumn(name = "crew_id", insertable = false, updatable = false)
    private Crew crew;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String chat;

    public CrewChat(Crew crew, Date date, String chat, Member member) {
        this.id = new CrewChatId(crew.getCrewId(), date); // Long id 전달
        System.out.println("CrewChat 생성자 test  " + crew.getCrewId());
        this.crew = crew;
        this.chat = chat;
        this.member = member;
    }
}
