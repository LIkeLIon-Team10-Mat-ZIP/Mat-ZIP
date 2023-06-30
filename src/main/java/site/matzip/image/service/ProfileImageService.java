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
            return;
        }

        String filename = "userId_" + member.getId();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, "profileImages/" + filename, multipartFile.getInputStream(), metadata);
        String imageUrl = amazonS3.getUrl(bucket, "profileImages/" + filename).toString();

        ProfileImage existingProfileImage = profileImageRepository.findByMember(member);

        if (existingProfileImage == null) {
            // 이미지가 없으면, 새로운 엔티티 생성하고 저장
            ProfileImage profileImage = ProfileImage.builder()
                    .imageUrl(imageUrl)
                    .originalImageName(multipartFile.getOriginalFilename())
                    .build();

            profileImage.setMember(member);

            profileImageRepository.save(profileImage);
        } else {
            // 이미지가 이미 있으면, 'imageUrl'과 'originalImageName' 업데이트
            existingProfileImage.modifyImageUrlAndOriginalName(imageUrl, multipartFile.getOriginalFilename());
        }

        log.info("Complete ProfileImage");

    }

    @Transactional
    public void deleteImage(String originalFilename) {
        amazonS3.deleteObject(bucket, originalFilename);
    }
}
