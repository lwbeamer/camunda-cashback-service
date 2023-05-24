package com.example.workflow.model;


import javax.persistence.*;
import lombok.Data;

import java.sql.Timestamp;


@Entity
@Table(name = "withdraw")
@Data
public class Withdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status withdrawStatus;

    private String credential;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    private Timestamp time;

    private double amount;


    private String stringIdentifier;

    private String errorMessage;

}
