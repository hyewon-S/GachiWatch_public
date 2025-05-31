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
    @EmbeddedId
    private CrewChatId id;

    //Date date;
    //Crew crew;
    String chat;
    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;

    public CrewChat(String chat) {
        this.chat = chat;
    }

    public CrewChat(Crew crew, Date date, String chat, Member member) {
        this.id = new CrewChatId(crew, date);
        this.chat = chat;
        this.member = member;
    }
}
