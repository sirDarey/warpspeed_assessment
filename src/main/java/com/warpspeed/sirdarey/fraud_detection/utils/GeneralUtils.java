package com.warpspeed.sirdarey.fraud_detection.utils;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GeneralUtils {

    public boolean isNotNull (Object object) {
        return !Objects.isNull(object);
    }
}
