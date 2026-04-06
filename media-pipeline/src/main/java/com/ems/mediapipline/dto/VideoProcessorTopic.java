package com.ems.mediapipline.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class VideoProcessorTopic {

    String mediaId;

    String fileName;

    String extension;

    Long fileAttachmentId;

    String privateKey;
}
