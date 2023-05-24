package com.example.workflow.model;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Table(name = "purchases")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marketplace_id")
    private Marketplace marketplace;


    @Enumerated(EnumType.STRING)
    private Status cashbackPaymentStatus;
    private boolean rulesRespected;

    private Timestamp timestamp;

    private double cashbackPercent;

    private double totalPrice;

    private String stringIdentifier;

    private String errorMessage;

}
