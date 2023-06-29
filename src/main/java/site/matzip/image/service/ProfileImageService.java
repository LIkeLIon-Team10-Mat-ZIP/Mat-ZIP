package site.matzip.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.matzip.image.domain.ProfileImage;
import site.matzip.image.repository.ProfileImageRepository;
import site.matzip.member.domain.Member;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileImageService {
    private final AmazonS3 amazonS3;
    private final ProfileImageRepository profileImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void saveProfileImage(MultipartFile multipartFile, Member member) throws IOException {
        if (multipartFile.isEmpty()) {
            log.info("Can't save ProfileImage (no image)");
        }

        String originalFilename = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);

        ProfileImage uploadImage = ProfileImage.builder()
                .imageUrl(amazonS3.getUrl(bucket, originalFilename).toString())
                .originalImageName(multipartFile.getOriginalFilename())
                .build();
        /**
         * 연관관계 편의 메서드 실행
         */
        profileImageRepository.save(uploadImage);

        log.info("Complete ProfileImage");
    }

    @Transactional
    public void deleteImage(String originalFilename) {

        amazonS3.deleteObject(bucket, originalFilename);
    }
}
