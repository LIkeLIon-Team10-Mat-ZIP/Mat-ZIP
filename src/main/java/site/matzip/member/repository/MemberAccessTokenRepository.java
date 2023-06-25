package site.matzip.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.matzip.member.domain.MemberAccessToken;

@Repository
public interface MemberAccessTokenRepository extends JpaRepository<MemberAccessToken, Long> {
}
