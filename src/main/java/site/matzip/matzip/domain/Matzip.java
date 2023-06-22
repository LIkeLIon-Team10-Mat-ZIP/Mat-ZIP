package site.matzip.matzip.domain;

import jakarta.persistence.*;
import lombok.*;
import site.matzip.member.domain.Member;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Matzip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matzipName;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private MatzipType matzipType;
    @OneToMany(mappedBy = "matzip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatzipRecommendation> matzipRecommendations;
}
