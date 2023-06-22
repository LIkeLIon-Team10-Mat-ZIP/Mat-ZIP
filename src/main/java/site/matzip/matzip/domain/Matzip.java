package site.matzip.matzip.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;


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
    private String description;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private MatzipType matzipType;
    private double rating;
}
