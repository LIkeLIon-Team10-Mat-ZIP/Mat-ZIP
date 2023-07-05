package site.matzip.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.matzip.friend.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
}
