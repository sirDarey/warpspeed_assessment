package com.warpspeed.sirdarey.fraud_detection.utils;

import com.warpspeed.sirdarey.fraud_detection.dto.TransactionEvent;
import com.warpspeed.sirdarey.fraud_detection.enums.FraudStatus;
import com.warpspeed.sirdarey.fraud_detection.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FraudDetectionUtils {

    private final TransactionService transactionService;


    public void checkAverageAmountWithinLast_24_Hours(TransactionEvent event, List<String> detectedFrauds) {
        //gets average of all transactions of this user in the last 24 hours from the timestamp of this event
        BigDecimal average = transactionService.getAverageAmountWithinLast_24_Hours(event);

        //detect fraud if current amount > 5* the average amount
        if (event.amount().compareTo(average.multiply(BigDecimal.valueOf(5))) > 0) {
            detectedFrauds.add(FraudStatus.AMOUNT_OVERFLOW.name());
        }
    }

    public void checkTransactionsInMultipleServices(TransactionEvent event, List<String> detectedFrauds) {
        //gets the list of serviceIDs of transactions of this user from 5 minutes ago to the timestamp of this event
        //Storing the service IDS in a set to prevent duplicates
        Set<String> serviceIDs = transactionService.getAllServiceIDsLessThan_5_MinutesAgo(event);
        serviceIDs.add(event.serviceID());  //adding the current serviceID so that when the services are > 3 (the current inclusive), then FLAG IT

        if (serviceIDs.size() > 3) { //transactions have occurred (or about to occur) in more than 3 services within 5 minutes window; Hence FLAG IT
            detectedFrauds.add(FraudStatus.MULTI_SERVICES.name());
        }
    }

    public void detectPingPong (TransactionEvent event, List<String> detectedFrauds) {

        //gets the Map of serviceIDs and their freq for transactions of this user from 10 minutes ago to the timestamp of this event
        Map<String, Integer> servicesMap = transactionService.getAllServicesLessThan_10_MinutesAgo(event);
        //adding the current event serviceID to the Map
        servicesMap.put(event.serviceID(), servicesMap.getOrDefault(event.serviceID(), 0) + 1);

        //check for possible ping-pong
        detectPingPong(servicesMap, detectedFrauds);
    }

    private void detectPingPong(Map<String, Integer> servicesMap, List<String> detectedFrauds) {
        int n = servicesMap.size();

        //services must be at least 2, for ping-pong to occur
        if (n >= 2) {

            //declaring an array if size n
            int [] arr = new int[n];
            int idx = 0;

            //saving the freq in the servicesMap into the array
            for (Integer freq : servicesMap.values()) {
                arr [idx++] = freq;
            }

            boolean breakFlag = false;

            //Nested loop to check for possible ping-pong
            for (int i=0; i<n-1; i++) {
                if (breakFlag) { // a flag to break out of this outer loop as soon as ping-pong is detected
                    break;
                }
                if (arr[i] < 2) {  //freq must be at least 2
                    continue;
                }

                for (int j=i+1; j<n; j++) {

                    /* ****** MAIN PING-PONG DETECTOR *********
                        Detect ping-pong if the difference between the frequencies of two services = 0.

                        This indicates that there is a sequence of similar transactions (by amount) from same user that has been going back and forth
                        between the 2 services.
                    **/
                    if (Math.abs(arr[i] - arr[j]) == 0) {  //freq of occurrence mus be the same to detect PING-PONG
                        detectedFrauds.add(FraudStatus.PING_PONG.name());
                        breakFlag = true;  //set flag to true in order for the outer loop to break
                        break;   //break out of the inner loop as soon as fraud is detected
                    }
                }
            }
        }
    }

    public List<TransactionEvent> getTransactionEventsFromInMemory () {
        return new ArrayList<>(List.of(
                new TransactionEvent(1617906000L, "user1", "serviceA", new BigDecimal("150.00")),

                new TransactionEvent(1617906020L, "user1", "serviceB", new BigDecimal("120.00")),

                new TransactionEvent(1617906040L, "user1", "serviceC", new BigDecimal("180.00")),

                new TransactionEvent(1617906060L, "user1", "serviceD", new BigDecimal("1500.00")),

                new TransactionEvent(1617906080L, "user2", "serviceA", new BigDecimal("100.00")),

                new TransactionEvent(1617906100L, "user2", "serviceB", new BigDecimal("200.00")),

                new TransactionEvent(1617906120L, "user2", "serviceC", new BigDecimal("200.00")),

                new TransactionEvent(1617906140L, "user2", "serviceD", new BigDecimal("300.00")),

                new TransactionEvent(1617906145L, "user3", "serviceA", new BigDecimal("200.00")),

                new TransactionEvent(1617906150L, "user3", "serviceB", new BigDecimal("200.00")),

                new TransactionEvent(1617906175L, "user3", "serviceA", new BigDecimal("200.00")),

                new TransactionEvent(1617906180L, "user3", "serviceB", new BigDecimal("200.00")),

                new TransactionEvent(1617906155L, "user4", "serviceA", new BigDecimal("200.00")),

                new TransactionEvent(1617906160L, "user4", "serviceB", new BigDecimal("300.00")),

                new TransactionEvent(1617906165L, "user4", "serviceC", new BigDecimal("250.00")),

                new TransactionEvent(1617906456L, "user4", "serviceD", new BigDecimal("200.00")),

                new TransactionEvent(1617906461L, "user4", "serviceA", new BigDecimal("200.00")),

                new TransactionEvent(1617906756L, "user4", "serviceB", new BigDecimal("200.00"))
        ));
    }
}
