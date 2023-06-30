package site.matzip.matzip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.matzip.domain.MatzipMember;

public interface MatzipMemberRepository extends JpaRepository<MatzipMember, Long> {
}
