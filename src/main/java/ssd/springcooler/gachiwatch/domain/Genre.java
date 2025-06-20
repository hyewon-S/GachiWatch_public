package ssd.springcooler.gachiwatch.domain;

import lombok.Getter;

@Getter
public enum Genre {
    SCIENCE_FICTION("SF", 878),
    TV_MOVIE("TV 영화", 10770),
    FAMILY("가족", 10751),
    HORROR("공포", 27),
    DOCUMENTARY("다큐", 99),
    DRAMA("드라마", 18),
    ROMANCE("로맨스", 10749),
    ADVENTURE("모험", 12),
    MYSTERY("미스터리", 9648),
    CRIME("범죄", 80),
    WESTERN("서부", 37),
    THRILLER("스릴러", 53),
    ANIMATION("애니메이션", 16),
    ACTION("액션", 28),
    HISTORY("역사", 36),
    MUSIC("음악", 10402),
    WAR("전쟁", 10752),
    COMEDY("코미디", 35),
    FANTASY("판타지", 14);

    private final String label;
    private final int genre_id;

    Genre(String label, int genre_id) {
        this.label = label;
        this.genre_id = genre_id;
    }

    public static Genre fromDisplayName(String displayName) {
        for (Genre p : Genre.values()) {
            if (p.label.equals(displayName)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown platform display name: " + displayName);
    }

    public static Genre fromGenreId(int tmdb_genre_id) {
        for (Genre p : Genre.values()) {
            if (p.genre_id == tmdb_genre_id) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown tmdb genre ID: " + tmdb_genre_id);
    }
}
