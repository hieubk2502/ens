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

    @PostMapping("/upload")
    public ResponseEntity<MediaUploadSdo> upload(@RequestParam MultipartFile file) throws MinioException, IOException {
        MediaUploadSdo result = mediaService.upload(file);

        return ResponseEntity.ok(result);
    }


}
