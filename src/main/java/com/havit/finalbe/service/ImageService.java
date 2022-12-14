package com.havit.finalbe.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.havit.finalbe.entity.Image;
import com.havit.finalbe.entity.RandomProfile;
import com.havit.finalbe.repository.ImageRepository;
import com.havit.finalbe.repository.RandomProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String havitbucket;
    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;
    private final RandomProfileRepository randomProfileRepository;


    public Long getImageId(MultipartFile multipartFile, String dirName) throws IOException {

        LocalDateTime now = LocalDateTime.now();

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("첨부된 이미지가 없습니다.");
        }

        Image image = Image.builder()
                .fileName(multipartFile.getOriginalFilename())
                .extension(multipartFile.getContentType())
                .savePath(now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .size(multipartFile.getSize())
                .build();
        imageRepository.save(image);

        String fileName = dirName + "/" + image.getImageId();

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(multipartFile.getSize());

        // S3 에 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(havitbucket, fileName, multipartFile.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return image.getImageId();
    }

    public String deleteImage(Long imageId) {

        Image originFile = imageRepository.findImageByImageId(imageId);
        if (null != originFile) {
            String key = "havit" + "/" + originFile.getImageId();
            amazonS3Client.deleteObject(havitbucket, key);

            imageRepository.delete(originFile);
            return "이미지 삭제가 완료되었습니다.";
        }
        return "삭제할 이미지가 없습니다.";
    }

    public Long randomImgUpload(MultipartFile multipartFile, String dirName) throws IOException {

        LocalDateTime now = LocalDateTime.now();

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("첨부된 이미지가 없습니다.");
        }

        RandomProfile randomProfile = RandomProfile.builder()
                .fileName(multipartFile.getOriginalFilename())
                .extension(multipartFile.getContentType())
                .savePath(now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .size(multipartFile.getSize())
                .build();
        randomProfileRepository.save(randomProfile);

        String fileName = dirName + "/" + randomProfile.getRandomId();

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(multipartFile.getSize());

        // S3 에 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(havitbucket, fileName, multipartFile.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return randomProfile.getRandomId();
    }


    public int randomImage() {

        Random random = new Random();

        List<RandomProfile> randomProfileList = randomProfileRepository.findAll();
        List<Long> randomIdList = new ArrayList<>();

        for (RandomProfile randomProfile : randomProfileList) {
            randomIdList.add(randomProfile.getRandomId());
        }

        int listSize = randomIdList.size();

        return random.nextInt(listSize) + 1;
    }
}
