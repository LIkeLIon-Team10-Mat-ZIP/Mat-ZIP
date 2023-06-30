package site.matzip.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.image.domain.ProfileImage;
import site.matzip.member.domain.Member;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    ProfileImage findByMember(Member member);
}
