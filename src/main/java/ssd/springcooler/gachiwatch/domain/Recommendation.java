package ssd.springcooler.gachiwatch.domain;

import java.util.List;

public class Recommendation {
    private int recommendationId;            // 추천 ID
    private Member member;                      // 추천을 받을 사용자 (비회원이면 null)
    private List<Content> contents; // 추천된 콘텐츠 리스트
    private RecommendationType type;        // 추천 유형 (TREND_BASED, GENRE_BASED)

    // 생성자 필요시 활성화
//    public Recommendation(int recommendationId, Member member, List<Content> contents, RecommendationType type) {
//        this.recommendationId = recommendationId;
//        this.member = member;
//        this.contents = contents;
//        this.type = type;
//    }

    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public RecommendationType getType() {
        return type;
    }

    public void setType(RecommendationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "recommendationId=" + recommendationId +
                ", member=" + member +
                ", contents=" + contents +
                ", type=" + type +
                '}';
    }
}
