package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CrewChatId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    public CrewChatId(Crew crew, Date date) {
        this.crew = crew;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrewChatId)) return false;
        CrewChatId that = (CrewChatId) o;
        return Objects.equals(crew, that.crew) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crew, date);
    }
}
