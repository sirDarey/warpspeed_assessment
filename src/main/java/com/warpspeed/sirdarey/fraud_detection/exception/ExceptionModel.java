package com.warpspeed.sirdarey.fraud_detection.exception;

public record ExceptionModel (
        int httpStatusCode,
        String message
) {}
