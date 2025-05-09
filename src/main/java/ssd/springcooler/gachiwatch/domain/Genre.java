package ssd.springcooler.gachiwatch.domain;

import lombok.Getter;

@Getter
public enum Genre {
    SCIENCE_FICTION("SF"),
    TV_MOVIE("TV 영화"),
    FAMILY("가족"),
    HORROR("공포"),
    DOCUMENTARY("다큐멘터리"),
    DRAMA("드라마"),
    ROMANCE("로맨스"),
    ADVENTURE("모험"),
    MYSTERY("미스터리"),
    CRIME("범죄"),
    WESTERN("서부"),
    THRILLER("스릴러"),
    ANIMATION("애니메이션"),
    ACTION("액션"),
    HISTORY("역사"),
    MUSIC("음악"),
    WAR("전쟁"),
    COMEDY("코미디"),
    FANTASY("판타지");

    private final String label;

    Genre(String label) {
        this.label = label;
    }

}
