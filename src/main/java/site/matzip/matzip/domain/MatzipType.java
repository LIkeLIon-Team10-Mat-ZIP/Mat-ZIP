package site.matzip.matzip.domain;

import lombok.Getter;

@Getter
public enum MatzipType {
    KOREAN("korean"),
    JAPANESE("japanese"),
    CHINESE("chinese"),
    ASIAN("asian"),
    WESTERN("western"),
    BAR("bar"),
    CAFE("cafe"),
    ETC("etc");
    private String value;

    private MatzipType(String value) {
        this.value = value;
    }
}
