package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "watched_content")
public class WatchedContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    //나중에 활성화 content에서 오류남
//    @ManyToOne
//    @JoinColumn(name = "content_id", nullable = false)
//    private Content content;
//
//    public WatchedContent(Member member, Content content) {
//        this.member = member;
//        this.content = content;
//    }

}
