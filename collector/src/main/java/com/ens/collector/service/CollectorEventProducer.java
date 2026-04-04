package com.ens.collector.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectorEventProducer {

    private final KafkaTemplate<String, com.google.protobuf.Message> kafkaTemplate;

    @Value("${collector.kafka.activity-topic:ens.collector.activity.daily}")
    private String activityTopic;

    @Value("${collector.kafka.marketing-topic:ens.collector.marketing.viewtime}")
    private String marketingTopic;

    public String publishDailyActivity(String jsonPayload) {
        DailyUserActivityReques message = parseDailyActivity(jsonPayload);
        kafkaTemplate.send(activityTopic, message.getUserId(), message);
        return activityTopic;
    }

    public String publishMarketingViewTime(String jsonPayload) {
        MarketingViewTimeRequest message = parseMarketingViewTime(jsonPayload);
        kafkaTemplate.send(marketingTopic, message.getUserId(), message);
        return marketingTopic;
    }

    private DailyUserActivityRequest parseDailyActivity(String jsonPayload) {
        if (jsonPayload == null || jsonPayload.isBlank()) {
            throw new IllegalArgumentException("Request body must be a JSON object.");
        }
        DailyUserActivityRequest.Builder builder = DailyUserActivityRequest.newBuilder();
        mergeJson(jsonPayload, builder);
        return builder.build();
    }

    private MarketingViewTimeRequest parseMarketingViewTime(String jsonPayload) {
        if (jsonPayload == null || jsonPayload.isBlank()) {
            throw new IllegalArgumentException("Request body must be a JSON object.");
        }
        MarketingViewTimeRequest.Builder builder = MarketingViewTimeRequest.newBuilder();
        mergeJson(jsonPayload, builder);
        return builder.build();
    }

    private void mergeJson(String jsonPayload, com.google.protobuf.Message.Builder builder) {
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(jsonPayload, builder);
        } catch (InvalidProtocolBufferException ex) {
            throw new IllegalArgumentException("Invalid JSON object for protobuf message.", ex);
        }
    }
}
