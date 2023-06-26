package site.matzip.matzip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.matzip.base.rsData.RsData;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.domain.MatzipRecommendation;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.repository.MatzipRecommendationRepository;
import site.matzip.matzip.repository.MatzipRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatzipService {
    private final MatzipRepository matzipRepository;
    private final MatzipRecommendationRepository matzipRecommendationRepository;

    public RsData<Matzip> create(MatzipCreationDTO creationDTO) {
        //맛집 등록
        Matzip matzip = Matzip.builder()
                .matzipName(creationDTO.getMatzipName())
                .address(creationDTO.getAddress())
                .matzipType(creationDTO.getMatzipTypeEnum())
                .phoneNumber(creationDTO.getPhoneNumber())
                .matzipUrl(creationDTO.getMatzipUrl())
                .x(creationDTO.getX())
                .y(creationDTO.getY())
                .build();
        Matzip savedMatzip = matzipRepository.save(matzip);
        //맛집 추천 생성
        MatzipRecommendation matzipRecommendation = MatzipRecommendation.builder()
                .rating(creationDTO.getRating())
                .description(creationDTO.getDescription())
                .matzip(savedMatzip)
                .build();
        matzipRecommendationRepository.save(matzipRecommendation);
        return RsData.of("S-1", "맛집이 등록 되었습니다.", savedMatzip);
    }

    public List<Matzip> findAllWithRecommendations() {
        return matzipRepository.findAllWithRecommendations();
    }

    public List<Matzip> findAll() {
        return matzipRepository.findAll();
    }
}
