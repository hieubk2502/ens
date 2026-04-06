package com.ems.mediapipline.processor;

import com.ems.mediapipline.dto.VideoProcessorTopic;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class VideoProcessor {

    @Value("${minio.bucket}")
    private String minioBucket;

    private final MinioClient minioClient;

    @KafkaListener(topics = "video-process-topic")
    public void process(VideoProcessorTopic videoProcessorTopic) throws Exception {

        String fileName = videoProcessorTopic.getFileName();
        String extension = videoProcessorTopic.getExtension();
        Long fileAttachmentId = videoProcessorTopic.getFileAttachmentId();
        String privateKey = videoProcessorTopic.getPrivateKey();

        // 1. Download file từ MinIO
        File inputFile = new File("/tmp/" + fileName);

        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioBucket)
                        .object("media/" + privateKey + "/raw/" + privateKey + extension)
                        .build());
             FileOutputStream fos = new FileOutputStream(inputFile)) {

            is.transferTo(fos);
        }

        // 2. Convert sang HLS
        File outputDir = new File("/tmp/hls/" + fileName);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        convertHlsMultiQuality(inputFile, outputDir);

        // 3. Upload lại HLS lên MinIO
        uploadFolder(privateKey, outputDir);

        // 4. Update DB
        updateDB(videoProcessorTopic.getFileAttachmentId());
    }
    private void updateDB(Long fileAttachmentId) {
    }

    private void uploadFolder(String mediaId, File dir) throws Exception {

        for (File file : FileUtils.listFiles(dir, null, true)) {

            String objectName = "hls/" + mediaId + "/" +
                    dir.toPath().relativize(file.toPath()).toString();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("videos")
                            .object(objectName)
                            .stream(new FileInputStream(file), file.length(), -1L)
                            .contentType("application/octet-stream")
                            .build()
            );
        }
    }

    private void convertHlsMultiQuality(File input, File outputDir) throws Exception {

        String cmd = "ffmpeg -i " + input.getAbsolutePath() +
                " -filter_complex " +
                "\"[0:v]split=3[v1][v2][v3];" +
                "[v1]scale=w=640:h=360[v1out];" +
                "[v2]scale=w=1280:h=720[v2out];" +
                "[v3]scale=w=1920:h=1080[v3out]\" " +

                "-map [v1out] -c:v:0 libx264 -b:v:0 800k " +
                "-map [v2out] -c:v:1 libx264 -b:v:1 2500k " +
                "-map [v3out] -c:v:2 libx264 -b:v:2 5000k " +

                "-map a:0 -c:a aac -b:a 128k " +

                "-f hls " +
                "-hls_time 6 " +
                "-hls_playlist_type vod " +
                "-master_pl_name master.m3u8 " +
                "-var_stream_map \"v:0,a:0 v:1,a:0 v:2,a:0\" " +

                outputDir.getAbsolutePath() + "/%v/index.m3u8";

        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
    }
}
