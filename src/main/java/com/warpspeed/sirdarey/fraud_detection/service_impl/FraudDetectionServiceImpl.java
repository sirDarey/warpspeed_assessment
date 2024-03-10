package com.warpspeed.sirdarey.fraud_detection.service_impl;

import com.warpspeed.sirdarey.fraud_detection.dto.FraudDetectionResponse;
import com.warpspeed.sirdarey.fraud_detection.dto.FraudulentDetails;
import com.warpspeed.sirdarey.fraud_detection.dto.TransactionEvent;
import com.warpspeed.sirdarey.fraud_detection.dto.TransactionEventRequest;
import com.warpspeed.sirdarey.fraud_detection.entity.Transaction;
import com.warpspeed.sirdarey.fraud_detection.repo.TransactionRepo;
import com.warpspeed.sirdarey.fraud_detection.service.FraudDetectionService;
import com.warpspeed.sirdarey.fraud_detection.utils.FraudDetectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final FraudDetectionUtils fraudDetectionUtils;
    private final TransactionRepo transactionRepo;


    @Override
    @Transactional
    public FraudDetectionResponse processEvents(TransactionEventRequest request) {

        //Sort the incoming request in order to handle OUT-OF-ORDER scenarios
        Collections.sort(request.transactions());

        List <FraudulentDetails> data = new ArrayList<>();

        //for each event in the transaction stream
        for (TransactionEvent event : request.transactions()) {

            List<String> detectedFrauds = new ArrayList<>();

            //check if amount is > than 5 * average amount in the last 24 hours
            fraudDetectionUtils.checkAverageAmountWithinLast_24_Hours(event, detectedFrauds);

            //check transactions in more than 3 distinct services within a 5-minute window
            fraudDetectionUtils.checkTransactionsInMultipleServices(event, detectedFrauds);

            //check for PING-PONG Activity
            fraudDetectionUtils.detectPingPong(event, detectedFrauds);

            //If the list of detected fraud based on the current transaction event is NOT EMPTY, add to list of FraudulentDetails
            if (!detectedFrauds.isEmpty()) {
                data.add(
                        new FraudulentDetails(event, detectedFrauds)
                );
            }

            //save this transaction
            transactionRepo.save(new Transaction(null, event.timestamp(), event.userID(), event.serviceID(), event.amount()));
        }

        FraudDetectionResponse response;
        if (data.isEmpty()) {
            response = new FraudDetectionResponse("Fraud Detection is: FALSE", null);
        } else {
            response = new FraudDetectionResponse("Fraud Detection is: TRUE", data);
        }

        //****** NOT TO BE USED IN PROD ************
        transactionRepo.deleteAll(); //Delete all records saved so that each new request can have a fresh/plain ground

        return response;
    }

    @Override
    public FraudDetectionResponse processEventsFromInMemoryQueue() {
        return processEvents(new TransactionEventRequest(fraudDetectionUtils.getTransactionEventsFromInMemory()));
    }

}
