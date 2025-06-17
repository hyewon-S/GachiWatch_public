package ssd.springcooler.gachiwatch.dto;

import lombok.Getter;

@Getter
public class EmailNotiDto {//이메일에 들어갈 message 관련 Dto
    //private String email;
    //private String content;
    private String title;
    private String genre;
    private String availablePlatform;

    public EmailNotiDto(String title, String genre, String availablePlatform) {
        this.title = title;
        this.genre = genre;
        this.availablePlatform = availablePlatform;
    }

}
