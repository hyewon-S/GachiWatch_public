package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class CrewChatId implements Serializable {
    private Long crewId;
    private Date chatDate;
    private Integer memberId;

    public CrewChatId() {}

    public CrewChatId(Long crewId, Date chatDate, Integer memberId) {
        this.crewId = crewId;
        this.chatDate = chatDate;
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrewChatId)) return false;
        CrewChatId that = (CrewChatId) o;
        return Objects.equals(crewId, that.crewId) &&
                Objects.equals(chatDate, that.chatDate) &&
                Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crewId, chatDate, memberId);
    }
}
/*
@Embeddable
@Access(AccessType.FIELD)
//@Getter
//@Setter
@NoArgsConstructor
public class CrewChatId implements Serializable {
@Column(name = "crew_id", nullable = false)
private Long crewId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "chat_date")
    private Date chatDate;

    public Date getChatDate() {
        return chatDate;
    }

    public void setChatDate(Date chatDate) {
        this.chatDate = chatDate;
    }

    public CrewChatId(Long crewId, Date chatDate) {
        this.crewId = crewId;
        this.chatDate = chatDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrewChatId)) return false;
        CrewChatId that = (CrewChatId) o;
        return Objects.equals(crewId, that.crewId) &&
                Objects.equals(chatDate, that.chatDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crewId, chatDate);
    }
}
*/