package site.matzip.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.image.domain.ProfileImage;
import site.matzip.member.domain.Member;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findByMember(Member member);
}
