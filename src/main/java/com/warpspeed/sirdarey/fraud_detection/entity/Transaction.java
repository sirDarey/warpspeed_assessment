package com.warpspeed.sirdarey.fraud_detection.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table (name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Transaction {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private Long timestamp;

    @Column (name = "user_id")
    private String userID;

    @Column (name = "service_id")
    private String serviceID;

    private BigDecimal amount;
}
