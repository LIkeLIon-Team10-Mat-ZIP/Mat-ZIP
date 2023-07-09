package site.matzip.badge.domain;

public enum BadgeType {
    REVIEWER("리뷰쓰는 나무늘보"),
    COMMENTER("댓글쓰는 강아지"),
    MAP_MASTER("맛집 개척의 돼지왕"),
    LOVED_ONE("과분한 하트"),
    LOTS_FRIENDS("곰에 곰잡고")
    ;

    private final String label;

    BadgeType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
