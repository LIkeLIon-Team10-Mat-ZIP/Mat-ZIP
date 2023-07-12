package site.matzip.matzip.domain;

import lombok.Getter;

@Getter
public enum MatzipType {
    KOREAN("한식"),
    JAPANESE("회/일식"),
    CHINESE("중식"),
    ASIAN("아시안"),
    WESTERN("양식"),
    BAR("술집"),
    CAFE("카페"),
    ETC("기타");
    private String value;

    private MatzipType(String value) {
        this.value = value;
    }
}
