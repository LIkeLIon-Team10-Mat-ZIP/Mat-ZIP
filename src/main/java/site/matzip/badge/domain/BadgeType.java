package site.matzip.badge.domain;

public enum BadgeType {
    REVIEWER("리뷰쓰는 나무늘보"),
    COMMENTER("댓글쓰는 강아지"),
    LOVED_ONE("좋아요"),
    MAP_MASTER("맛집 개척의 돼지왕")
    ;

    private final String label;

    BadgeType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
