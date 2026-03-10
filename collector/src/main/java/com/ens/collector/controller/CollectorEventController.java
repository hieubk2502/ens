package com.ens.collector.controller;

import com.ens.collector.dto.CollectorResponse;
import com.ens.collector.service.CollectorEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/collect")
@RequiredArgsConstructor
public class CollectorEventController {

    private final CollectorEventProducer producer;

    @PostMapping("/activity-daily")
    public ResponseEntity<CollectorResponse> collectDailyActivity(@RequestBody String payload) {
        return publish(payload, true);
    }

    @PostMapping("/marketing-view-time")
    public ResponseEntity<CollectorResponse> collectMarketingViewTime(@RequestBody String payload) {
        return publish(payload, false);
    }

    private ResponseEntity<CollectorResponse> publish(String payload, boolean dailyActivity) {
        try {
            String topic = dailyActivity
                    ? producer.publishDailyActivity(payload)
                    : producer.publishMarketingViewTime(payload);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new CollectorResponse(topic, "queued"));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
