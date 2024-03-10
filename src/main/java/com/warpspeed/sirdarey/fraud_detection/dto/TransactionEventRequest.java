package com.warpspeed.sirdarey.fraud_detection.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TransactionEventRequest(

        @NotNull (message = "transactions list is required")
        @NotEmpty(message = "transactions list is required")
        List <TransactionEvent> transactions

){}

