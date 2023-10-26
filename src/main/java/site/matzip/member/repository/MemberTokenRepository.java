package site.matzip.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.matzip.member.domain.MemberToken;

import java.util.Optional;

@Repository
public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {

    Optional<MemberToken> findByMemberId(Long memberId);
}
