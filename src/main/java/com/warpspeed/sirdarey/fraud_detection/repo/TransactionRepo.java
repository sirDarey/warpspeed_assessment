package com.warpspeed.sirdarey.fraud_detection.repo;

import com.warpspeed.sirdarey.fraud_detection.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


public interface TransactionRepo extends JpaRepository <Transaction, Long> {

    @Query (nativeQuery = true, value = "SELECT service_id FROM transactions WHERE user_id=?1 AND timestamp>=?2 AND timestamp<=?3")
    Set<String> getAllServiceIDsLessThan_5_MinutesAgo(String userID, Long fiveMinutesAgo, Long eventTimestamp);

    @Query (nativeQuery = true, value = "SELECT service_id FROM transactions WHERE user_id=?1 AND amount=?2 AND timestamp>=?3 AND timestamp <=?4")
    List<String> getAllServicesLessThan_10_MinutesAgo(String userID, BigDecimal amount, Long tenMinutesAgo, Long eventTimestamp);

    @Query(nativeQuery = true, value = "SELECT AVG(amount) FROM transactions WHERE user_id=?1 AND timestamp >=?2 AND timestamp<=?3")
    BigDecimal getAverageAmountWithinLast_24_Hours(String userID, Long last_24_hours, Long eventTimestamp);
}
