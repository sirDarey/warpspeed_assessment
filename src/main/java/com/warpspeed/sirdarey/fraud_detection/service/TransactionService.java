package com.warpspeed.sirdarey.fraud_detection.service;

import com.warpspeed.sirdarey.fraud_detection.dto.TransactionEvent;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public interface TransactionService {

    Set<String> getAllServiceIDsLessThan_5_MinutesAgo(TransactionEvent event);

    Map<String, Integer> getAllServicesLessThan_10_MinutesAgo (TransactionEvent event);

    BigDecimal getAverageAmountWithinLast_24_Hours (TransactionEvent event);
}
