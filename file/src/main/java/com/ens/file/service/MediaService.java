package com.ens.file.service;

import com.ens.file.dto.file.sdo.MediaUploadSdo;
import io.minio.errors.MinioException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaService {
     MediaUploadSdo uploadV1(MultipartFile file) throws IOException, MinioException;
     MediaUploadSdo uploadV2(MultipartFile file) throws IOException, MinioException;
}
