package site.matzip.matzip.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matzip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matzipName;
    private String description;
    private String address;
    @Enumerated(EnumType.STRING)
    private MatzipType matzipType;
    private LocalTime openingTime;
    private LocalTime closingTime;

}
