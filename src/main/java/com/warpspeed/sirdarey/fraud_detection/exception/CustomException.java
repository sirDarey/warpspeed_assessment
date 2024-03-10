package com.warpspeed.sirdarey.fraud_detection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor @Getter @AllArgsConstructor
public class CustomException extends RuntimeException{

    private int httpStatusCode;

    private String message;
}
