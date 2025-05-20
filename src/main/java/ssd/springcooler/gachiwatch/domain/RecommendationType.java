package ssd.springcooler.gachiwatch.domain;

import lombok.Getter;

@Getter
public enum RecommendationType {
    TREND_BASED("트렌드 기반 추천"), //트렌드 기반 추천 (비회원 가능)
    GENRE_BASED("장르 기반 추천"); //사용자 선호 장르 기반 추천 (회원 전용)

    private final String label;

    RecommendationType(String label) {
        this.label = label;
    }
}
