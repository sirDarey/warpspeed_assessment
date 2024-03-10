package com.warpspeed.sirdarey.fraud_detection.dto;


import java.math.BigDecimal;
import java.math.RoundingMode;

public record TransactionEvent(Long timestamp, String userID, String serviceID, BigDecimal amount) implements Comparable<TransactionEvent> {

        //For sorting in ascending order based on the timestamp
        @Override
        public int compareTo(TransactionEvent event) {
                return this.timestamp.compareTo(event.timestamp);
        }

        public TransactionEvent(Long timestamp, String userID, String serviceID, BigDecimal amount) {
                this.timestamp = timestamp;
                this.userID = userID;
                this.serviceID = serviceID;
                this.amount = amount.setScale(2, RoundingMode.CEILING);
        }
}
