package com.ens.collector.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectorProducer {

    private final KafkaTemplate<String, com.google.protobuf.Message> kafkaTemplate;

    @Value("${collector.kafka.topic:ens.collector.raw}")
    private String defaultTopic;

    public String publish(String topic, String jsonPayload) {
        if (jsonPayload == null || jsonPayload.isBlank()) {
            throw new IllegalArgumentException("Request body must be a JSON object.");
        }
        Struct payload = toStruct(jsonPayload);
        String targetTopic = (topic == null || topic.isBlank()) ? defaultTopic : topic;
        kafkaTemplate.send(targetTopic, payload);
        return targetTopic;
    }

    private Struct toStruct(String jsonPayload) {
        Struct.Builder builder = Struct.newBuilder();
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(jsonPayload, builder);
        } catch (InvalidProtocolBufferException ex) {
            throw new IllegalArgumentException("Invalid JSON object for protobuf Struct.", ex);
        }
        return builder.build();
    }
}
