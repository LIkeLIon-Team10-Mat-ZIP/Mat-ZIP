package site.matzip.matzip.domain;

import jakarta.persistence.*;
import lombok.*;
import site.matzip.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public MatzipMember(String description, double rating) {
        this.description = description;
        this.rating = rating;
    }

    public void addAssociation(Member author, Matzip matzip) {
        addAuthor(author);
        addMatzip(matzip);
    }

    private void addAuthor(Member author) {
        if (this.author != null) {
            this.author.getMatzipMembers().remove(this);
        }
        this.author = author;
        author.getMatzipMembers().add(this);
    }

    private void addMatzip(Matzip matzip) {
        if (this.matzip != null) {
            this.matzip.getMatzipMembers().remove(this);
        }
        this.matzip = matzip;
        matzip.getMatzipMembers().add(this);
    }

    public void modify(String description, double rating) {
        this.rating = rating;
        this.description = description;
    }
}
