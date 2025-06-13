package ssd.springcooler.gachiwatch.domain;

import lombok.Getter;

@Getter
public enum Platform {
    NETFLIX("넷플릭스", 8),
    WATCHA("왓챠", 97),
    DISNEY("디즈니+", 337),
    APPLE("애플티비+", 350),
    WAVVE("웨이브", 356),
    AMAZON("아마존 프라임", 119),
    NULL("현재 제공되는 플랫폼 없음", 0);

    private final String label;
    private final int platformId;

    Platform(String label, int platformId) {
        this.label = label;
        this.platformId = platformId;
    }

    public static Platform fromDisplayName(String displayName) {
        for (Platform p : Platform.values()) {
            if (p.label.equalsIgnoreCase(displayName)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown platform display name: " + displayName);
    }

    public static Platform fromPlatformId(int platformId) {
        for (Platform p : Platform.values()) {
            if (p.platformId == platformId) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown platform ID: " + platformId);
    }
}
