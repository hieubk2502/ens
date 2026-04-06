package com.ens.file.service.impl;

import com.ens.file.dto.file.sdo.MediaUploadSdo;
import com.ens.file.service.MediaService;
import com.ens.file.validator.MediaValidator;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    @Value("${minio.bucket}")
    private String minioBucket;

    private final MinioClient minioClient;

    @Override
    public MediaUploadSdo uploadV1(MultipartFile file) throws IOException, MinioException {

        MediaValidator.validate(file);

        // save db and set status is temp ( will have job delete file temp every day)


        Long fileAttachmentId = Random.from(RandomGenerator.getDefault()).nextLong();
        String privateKey = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        String contentType = file.getContentType();

        var object = PutObjectArgs.builder()
                .bucket(minioBucket)
                .object("media/" + privateKey + "/raw/" + privateKey + extension)
                .contentType(contentType)
                // minio will auto parse video to multipart and upload to minio server
                .stream(file.getInputStream(), file.getSize(), -1L)
                .build();

        minioClient.putObject(object);

        String publicKey = genPublicKey(privateKey);

        return MediaUploadSdo.builder().publicKey(publicKey).build();
    }

    private String genPublicKey(String privateKey) {

        // will do later
        return privateKey;
    }

    @Override
    public MediaUploadSdo uploadV2(MultipartFile file) throws IOException, MinioException {
        return null;
    }
}
