package com.ens.file.validator;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class MediaValidator {
    private static final Set<String> ALLOWED_EXT = Set.of("mp4", "mov", "mkv");

    private static final Set<String> ALLOWED_MIME = Set.of(
            "video/mp4",
            "video/quicktime",
            "video/x-matroska"
    );

    private static final long MAX_SIZE = 500 * 1024 * 1024; // 500MB

    public static void validate(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("File too large");
        }

        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();

        // Layer 1: check extension
        if (!ALLOWED_EXT.contains(extension)) {
            throw new RuntimeException("Invalid file extension");
        }

        // Layer 2: check content Type
        // check contentType từ client
        String contentType = file.getContentType();
        if (!ALLOWED_MIME.contains(contentType)) {
            throw new RuntimeException("Invalid content type");
        }


        try(InputStream inputStream = file.getInputStream()) {

            // Layer 3: check magic number
            // Valid magic number is valid signature header at head file
            // Every files ( mp4, png, pdf...) will have signature byte at head file
            // not dêpend on file or extension
            byte[] header = new byte[12];
            inputStream.read(header);

            if (!isValidMediaHeader(header)) {
                throw new RuntimeException("Invalid video file ( fake file)");
            }

            // Layer 4.  Check MINE using TIKA
            // check contentType thật từ file
            // tika will read all file => dêtect contentType real.
            String mine = new Tika().detect(inputStream);

            if (!mine.startsWith("video/")) {
                throw new RuntimeException("Not a video");
            }

            // layer 5: parse video
            if (!isRealMedia(file)) {
                throw new RuntimeException("Parse: Not a video");
            }
        }
        

    }

    private static boolean isRealMedia(MultipartFile file) {
        // using ffprobe
        return true;

    }

    private static boolean isValidMediaHeader(byte[] header) {

        String headerStr = new String(header);

        // MP4: co "ftyp"
        if (headerStr.contains("ftyp")) return true;

        // MKV: starts with 0x1A45DFA3
        if ((header[0] & 0xFF) == 0x1A &&
                (header[1] & 0xFF) == 0x45) return true;

        return false;
    }

}
