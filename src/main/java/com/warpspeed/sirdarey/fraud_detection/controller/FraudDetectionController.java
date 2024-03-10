package com.warpspeed.sirdarey.fraud_detection.controller;

import com.warpspeed.sirdarey.fraud_detection.dto.FraudDetectionResponse;
import com.warpspeed.sirdarey.fraud_detection.dto.TransactionEventRequest;
import com.warpspeed.sirdarey.fraud_detection.service.FraudDetectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class FraudDetectionController {

    private final FraudDetectionService fraudDetectionService;

    @PostMapping
    public FraudDetectionResponse processEvents (@RequestBody @Valid TransactionEventRequest request) {
        return fraudDetectionService.processEvents(request);
    }

    @GetMapping
    public FraudDetectionResponse processEventsFromInMemoryQueue () {
        return fraudDetectionService.processEventsFromInMemoryQueue();
    }
}
