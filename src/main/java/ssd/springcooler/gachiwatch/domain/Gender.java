package ssd.springcooler.gachiwatch.domain;

import lombok.Getter;

@Getter
public enum Gender {
    FEMALE(1),
    MALE(2),
    NO_INFO(3);

    private final int code;
    //private final String label;

    Gender(int code/*, String label*/) {
        this.code = code;
        //this.label = label;
    }

    public static Gender fromCode(int code) {
        for (Gender gender : values()) {
            if (gender.code == code) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown Gender code: " + code);
    }

    public Integer getCode() {
        return this.code;
    }
}