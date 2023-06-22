package site.matzip.matzip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.repository.MatzipRepository;
import site.matzip.base.rsData.RsData;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatzipService {
    private final MatzipRepository matzipRepository;

    public RsData<Matzip> create(MatzipCreationDTO creationDTO) {
        Matzip matzip = Matzip.builder()
                .matzipName(creationDTO.getMatzipName())
                .address(creationDTO.getAddress())
                .description(creationDTO.getDescription())
                .matzipType(creationDTO.getMatzipTypeEnum())
                .openingTime(creationDTO.getOpeningTime())
                .closingTime(creationDTO.getClosingTime())
                .phoneNumber(creationDTO.getPhoneNumber())
                .build();

        Matzip savedMatzip = matzipRepository.save(matzip);
        return RsData.of("S-1", "맛집이 등록 되었습니다.",savedMatzip);
    }

    public List<Matzip> findAll() {
        List<Matzip> list = matzipRepository.findAll();
        return list;
    }
}
