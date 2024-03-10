package com.warpspeed.sirdarey.fraud_detection.dto;

import java.util.List;

public record FraudulentDetails(

        TransactionEvent eventTrigger,

        List <String> detectedFrauds
) {}
