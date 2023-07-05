package site.matzip.matzip.dto;

import lombok.Data;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.domain.MatzipType;

@Data
public class MatzipInfoDTO {
    private Long matzipId;
    private String matzipName;
    private MatzipType matzipType;
    private String address;
    private String phoneNumber;
    private String matzipUrl;

    public MatzipInfoDTO(Matzip matzip) {
        this.matzipId = matzip.getId();
        this.matzipName = matzip.getMatzipName();
        this.matzipType = matzip.getMatzipType();
        this.address = matzip.getAddress();
        this.phoneNumber = matzip.getPhoneNumber();
        this.matzipUrl = matzip.getMatzipUrl();
    }
}