package com.warpspeed.sirdarey.fraud_detection.dto;

import java.util.List;

public record FraudDetectionResponse(
        String message,
        List <FraudulentDetails> data
) {}
