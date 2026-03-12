package com.ens.collector.controller;

import com.ens.collector.dto.CollectorResponse;
import com.ens.collector.service.CollectorProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/collect")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorProducer producer;

    @PostMapping
    public ResponseEntity<CollectorResponse> collect(
            @RequestBody String payload,
            @RequestParam(value = "topic", required = false) String topic
    ) {
        return publishAndRespond(payload, topic);
    }

    @PostMapping("/{topic}")
    public ResponseEntity<CollectorResponse> collectToTopic(
            @PathVariable String topic,
            @RequestBody String payload
    ) {
        return publishAndRespond(payload, topic);
    }

    private ResponseEntity<CollectorResponse> publishAndRespond(String payload, String topic) {
        try {
            String targetTopic = producer.publish(topic, payload);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new CollectorResponse(targetTopic, "queued"));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
