package com.ems.mediapipline.config;


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value(value = "minio.url")
    String minioUrl;

    @Value(value = "minio.access_key")
    String minioAccessKey;

    @Value(value = "minio.private_key")
    String minioPrivateKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioAccessKey, minioPrivateKey)
                .build();
    }


}
