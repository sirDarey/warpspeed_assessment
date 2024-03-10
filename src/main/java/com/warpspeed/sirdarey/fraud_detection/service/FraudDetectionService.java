package com.warpspeed.sirdarey.fraud_detection.service;

import com.warpspeed.sirdarey.fraud_detection.dto.FraudDetectionResponse;
import com.warpspeed.sirdarey.fraud_detection.dto.TransactionEventRequest;

public interface FraudDetectionService {

    FraudDetectionResponse processEvents(TransactionEventRequest request );

    FraudDetectionResponse processEventsFromInMemoryQueue();
}
