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
import java.util.Objects;

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
        for (int index = 0; index < multipartFiles.size(); index++) {
            if (multipartFiles.get(index).getOriginalFilename() != null &&
                    !Objects.requireNonNull(multipartFiles.get(index).getOriginalFilename()).isEmpty()) {
                saveReviewImage(multipartFiles.get(index), review, index);
            }
        }

        log.info("Complete Save Review Images");
    }

    private void saveReviewImage(MultipartFile multipartFile, Review review, int index) throws IOException {

        String filename = "reviewId_" + review.getId() + "_" + (index + 1);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, "reviewImages/" + filename, multipartFile.getInputStream(), metadata);

        ReviewImage uploadImage = ReviewImage.builder()
                .imageUrl(amazonS3.getUrl(bucket, "reviewImages/" + filename).toString())
                .originalImageName(filename)
                .build();
        uploadImage.setReview(review);
        reviewImageRepository.save(uploadImage);
    }

    @Transactional
    public void modify(List<MultipartFile> multipartFiles, Review review) throws IOException {
        deleteForUpdate(review);
        review.getReviewImages().clear();

        create(multipartFiles, review);
    }

    private void deleteForUpdate(Review review) {
        List<ReviewImage> findReviewImages = review.getReviewImages();

        for (ReviewImage findReviewImage : findReviewImages) {
            delete(findReviewImage.getOriginalImageName());
        }
    }

    private void delete(String orginalFilename) {
        amazonS3.deleteObject(bucket, "reviewImages/" + orginalFilename);
    }

    @Transactional
    public void remove(Review review) {
        deleteForUpdate(review);
    }
}
