package site.matzip.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.image.domain.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
