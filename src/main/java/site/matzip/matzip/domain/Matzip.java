package site.matzip.matzip.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.matzip.review.domain.Review;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Matzip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matzipName;
    private String address;
    private String phoneNumber;
    private String matzipUrl;
    private Long kakaoId;
    @Enumerated(EnumType.STRING)
    private MatzipType matzipType;
    private double x;
    private double y;
    @OneToMany(mappedBy = "matzip", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "matzip", cascade = CascadeType.ALL)
    private List<MatzipMember> matzipMembers = new ArrayList<>();


    @Builder
    public Matzip(String matzipName, String address, String phoneNumber,
                  String matzipUrl, Long kakaoId, MatzipType matzipType,
                  double x, double y) {
        this.matzipName = matzipName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.matzipUrl = matzipUrl;
        this.kakaoId = kakaoId;
        this.matzipType = matzipType;
        this.x = x;
        this.y = y;
    }
}
