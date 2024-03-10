package com.warpspeed.sirdarey.fraud_detection.service_impl;

import com.warpspeed.sirdarey.fraud_detection.dto.TransactionEvent;
import com.warpspeed.sirdarey.fraud_detection.repo.TransactionRepo;
import com.warpspeed.sirdarey.fraud_detection.service.TransactionService;
import com.warpspeed.sirdarey.fraud_detection.utils.GeneralUtils;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    public final GeneralUtils generalUtils;

    @Override
    @Nonnull
    public Set<String> getAllServiceIDsLessThan_5_MinutesAgo(TransactionEvent event) {
        if (generalUtils.isNotNull(event.timestamp())) {
            Long fiveMinutesAgo = event.timestamp() - (5*60);
            return transactionRepo.getAllServiceIDsLessThan_5_MinutesAgo(event.userID(), fiveMinutesAgo, event.timestamp());
        }
        return new HashSet<>();
    }

    @Override
    @Nonnull
    public Map<String, Integer> getAllServicesLessThan_10_MinutesAgo(TransactionEvent event) {
        Map<String, Integer> serviceMap = new HashMap<>();

        if (generalUtils.isNotNull(event.timestamp())) {
            Long tenMinutesAgo = event.timestamp() - (10*60);

            transactionRepo
                    .getAllServicesLessThan_10_MinutesAgo(event.userID(), event.amount(), tenMinutesAgo, event.timestamp())
                    .forEach(service -> serviceMap.put(service, serviceMap.getOrDefault(service, 0)+1));
        }
        return serviceMap;
    }

    @Override
    @Nonnull
    public BigDecimal getAverageAmountWithinLast_24_Hours(TransactionEvent event) {
        if (generalUtils.isNotNull(event.amount())) {
            Long last_24_hours = event.timestamp() - (24*60*60);
            BigDecimal average = transactionRepo.getAverageAmountWithinLast_24_Hours (event.userID(), last_24_hours, event.timestamp());
            return (average == null)? event.amount(): average;
        }
        return event.amount();
    }
}
