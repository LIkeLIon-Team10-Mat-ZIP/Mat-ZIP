package site.matzip.matzip.domain;

import jakarta.persistence.*;
import lombok.*;
import site.matzip.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MatzipMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private double rating;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matzip_id")
    private Matzip matzip;

    public void update(String description, double rating) {
        this.rating = rating;
        this.description = description;
    }

    public void setAuthor(Member author) {
        if (this.author != null) {
            this.author.getMatzipMembers().remove(this);
        }
        this.author = author;
        author.getMatzipMembers().add(this);
    }

    public void setMatzip(Matzip matzip) {
        if (this.matzip != null) {
            this.matzip.getMatzipMembers().remove(this);
        }
        this.matzip = matzip;
        author.getMatzipMembers().add(this);
    }
}
