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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaServiceImpl implements MediaService {

    @Value("minio.bucket")
    String minioBucket;

    MinioClient minioClient;

    @Override
    public MediaUploadSdo upload(MultipartFile file) throws IOException, MinioException {

        MediaValidator.validate(file);

        String mediaId = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        String contentType = file.getContentType();

        var object = PutObjectArgs.builder()
                .bucket(minioBucket)
                .object("raw/" + mediaId + extension)
                .contentType(contentType)
                // minio will auto parse video to multipart and upload to minio server
                .stream(file.getInputStream(), file.getSize(), -1L)
                .build();

        minioClient.putObject(object);

        return MediaUploadSdo.builder().mediaId(mediaId).build();
    }
}
