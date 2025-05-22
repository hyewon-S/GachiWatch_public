package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "liked_content")
public class LikedContent {
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
//    @Column(name = "liked_order")
//    private int likedOrder;
//
//    public LikedContent(Member member, Content content, int likedOrder) {
//        this.member = member;
//        this.content = content;
//        this.likedOrder = likedOrder;
//    }

}
