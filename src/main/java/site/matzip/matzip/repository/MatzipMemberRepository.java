package site.matzip.matzip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.matzip.domain.MatzipMember;

import java.util.Optional;

public interface MatzipMemberRepository extends JpaRepository<MatzipMember, Long> {
    Optional<MatzipMember> findByMatzipIdAndMemberId(Long matzipId, Long memberId);
}
