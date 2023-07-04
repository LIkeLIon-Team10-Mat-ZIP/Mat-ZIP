package site.matzip.badge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.matzip.badge.repository.BadgeRepository;
import site.matzip.badge.repository.MemberBadgeRepository;

@Service
@RequiredArgsConstructor
public class MemberBadgeService {

    private final BadgeRepository badgeRepository;
    private final MemberBadgeRepository memberBadgeRepository;
}
