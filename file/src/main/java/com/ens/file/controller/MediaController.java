package com.ens.file.controller;

import com.ens.file.dto.file.sdo.MediaUploadSdo;
import com.ens.file.service.MediaService;
import io.minio.errors.MinioException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaController {
    MediaService mediaService;

    @PostMapping("/upload/v1")
    public ResponseEntity<MediaUploadSdo> uploadV1(@RequestParam MultipartFile file) throws MinioException, IOException {
        MediaUploadSdo result = mediaService.uploadV1(file);

        return ResponseEntity.ok(result);
    }
    @PostMapping("/upload/v2")
    public ResponseEntity<MediaUploadSdo> uploadV2(@RequestParam MultipartFile file) throws MinioException, IOException {
        MediaUploadSdo result = mediaService.uploadV2(file);

        return ResponseEntity.ok(result);
    }

}
