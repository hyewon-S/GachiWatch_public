package ssd.springcooler.gachiwatch.domain;

import lombok.Getter;

@Getter
public enum Platform {
    NETFLIX("넷플릭스"),
    WATCHA("왓챠"),
    DISNEY("디즈니+"),
    APPLE("애플티비+"),
    WAVVE("웨이브"),
    AMAZON("아마존 프라임");

    private final String label;

    Platform(String label) {
        this.label = label;
    }

}
