package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review_like")
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "review_id")
    private int reviewId;

    @Column(name = "member_id")
    private int memberId;

    public ReviewLike(int reviewId, int memberId) {
        this.reviewId = reviewId;
        this.memberId = memberId;
    }
}
