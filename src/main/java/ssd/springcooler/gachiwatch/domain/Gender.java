package ssd.springcooler.gachiwatch.domain;

import lombok.Getter;

@Getter
public enum Gender {
    FEMALE("여성"),
    MALE("남성"),
    NO_INFO("선택 안함");

    private final String label;

    Gender(String label) {
        this.label = label;
    }
}