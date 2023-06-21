package site.matzip.matzip.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class Matzip {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createDate;
    private String matzipName;
    private String description;
    private String address;
    @Enumerated(EnumType.STRING)
    private MatzipType matzipType;
    private LocalTime openingTime;
    private LocalTime closingTime;


}
