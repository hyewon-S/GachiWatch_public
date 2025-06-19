package ssd.springcooler.gachiwatch.dto;

import lombok.*;
import ssd.springcooler.gachiwatch.domain.Review;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberReviewDto {
    private int reviewId;
    private int contentId;
    private String contentTitle;
    private String contentImgUrl;
    private String substance;
    private String date;
    private int score;
    private int likes;
    private String nickname;

    public static MemberReviewDto fromEntity(Review review) {
        return MemberReviewDto.builder()
                .reviewId(review.getReviewId())
                .contentId(review.getContentId())
                .contentTitle(review.getContent() != null ? review.getContent().getTitle() : null)
                .contentImgUrl(review.getContent() != null ? review.getContent().getImgUrl() : null)
                .substance(review.getSubstance())
                .date(review.getDate())
                .score(review.getScore())
                .likes(review.getLikes())
                .nickname(review.getMember().getNickname())
                .build();
    }
}
