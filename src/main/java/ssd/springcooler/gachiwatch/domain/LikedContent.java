package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member_liked_content")
public class LikedContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 대응
    private int id;

    @Column(name = "member_id")
    private int memberId;

    @Column(name = "content_id")
    private int contentId;

    public LikedContent(int memberId, int contentId) {
        this.memberId = memberId;
        this.contentId = contentId;
    }
}

