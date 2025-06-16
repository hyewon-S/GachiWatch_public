package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(CrewChatId.class)
@Table(name = "CREWCHAT")
public class CrewChat {
//@EmbeddedId
//private CrewChatId id;

    @Id
    @Column(name = "CREW_ID")
    private Long crewId;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHAT_DATE")
    private Date chatDate;

    @Id
    @Column(name = "MEMBER_ID")
    private Integer memberId;

    @Column(name = "CHAT", nullable = false)
    private String chat;

    // 연관 관계
    @Transient
    @ManyToOne
    @JoinColumn(name = "CREW_ID", insertable = false, updatable = false)
    private Crew crew;

    @Transient
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", insertable = false, updatable = false)
    private Member member;

    public CrewChat(Crew crew, Date date, String chat, Member member) {
        this.crew = crew;
        this.crewId = crew.getCrewId();
        this.chatDate = date;
        this.chat = chat;
        this.member = member;
        this.memberId = (Integer) member.getMemberId();
    }
/*
    @ManyToOne
    @MapsId("crewId") // EmbeddedId의 필드 이름과 매핑
    @JoinColumn(name = "crew_id", insertable = false, updatable = false)
    private Crew crew;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String chat;

    public CrewChat(Crew crew, Date date, String chat, Member member) {
        this.id = new CrewChatId(crew.getCrewId(), date, member.getMemberId()); // Long id 전달
        System.out.println("CrewChat 생성자 test  " + crew.getCrewId());
        this.crew = crew;
        this.chat = chat;
        this.member = member;
    }*/
}
