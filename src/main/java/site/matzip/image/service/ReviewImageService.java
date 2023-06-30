package site.matzip.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.matzip.image.domain.ReviewImage;
import site.matzip.image.repository.ReviewImageRepository;
import site.matzip.review.domain.Review;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewImageService {

    private final AmazonS3 amazonS3;
    private final ReviewImageRepository reviewImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void create(List<MultipartFile> multipartFiles, Review review) throws IOException {
        if (multipartFiles.size() == 0) {
            log.info("Can't save Review Images (no image)");
        }
        for (MultipartFile multipartFile : multipartFiles) {
            saveReviewImage(multipartFile, review);
        }

        log.info("Complete Review Images");
    }

    private void saveReviewImage(MultipartFile multipartFile, Review review) throws IOException {
        String originalFilename = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);

        ReviewImage uploadImage = ReviewImage.builder()
                .imageUrl(amazonS3.getUrl(bucket, originalFilename).toString())
                .originalImageName(multipartFile.getOriginalFilename())
                .build();
        uploadImage.setReview(review);
        reviewImageRepository.save(uploadImage);
    }

    @Transactional
    public void deleteImage(String originalFilename) {

        amazonS3.deleteObject(bucket, originalFilename);
    }
}
