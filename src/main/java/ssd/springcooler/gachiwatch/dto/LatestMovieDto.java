package ssd.springcooler.gachiwatch.dto;

public class LatestMovieDto {
    private final int id;
    private final String posterPath;

    public LatestMovieDto(int id, String posterPath) {
        this.id = id;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getImageUrl() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }
}
