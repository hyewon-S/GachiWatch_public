package ssd.springcooler.gachiwatch.dto;
import lombok.*;

public class ForMeContentDto {
    private final int id;
    private final String posterPath;
    private final String mediaType; // "movie" 또는 "tv"

    public ForMeContentDto(int id, String posterPath, String mediaType) {
        this.id = id;
        this.posterPath = posterPath;
        this.mediaType = mediaType;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getImageUrl() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }
}
