package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member_watched_content")
public class WatchedContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "member_id")
    private int memberId;

    @Column(name = "content_id")
    private int contentId;

    public WatchedContent(int memberId, int contentId) {
        this.memberId = memberId;
        this.contentId = contentId;
    }

}
