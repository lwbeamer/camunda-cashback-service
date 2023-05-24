package com.example.workflow.dto;

import lombok.Data;

@Data
public class PurchaseFromMarketplaceDto {
    private double cashbackPercent;
    private double totalPrice;
    private long marketplaceId;
    private String username;

    private String stringIdentifier;

}
