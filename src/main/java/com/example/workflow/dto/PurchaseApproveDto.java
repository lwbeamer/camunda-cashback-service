package com.example.workflow.dto;

import lombok.Data;

@Data
public class PurchaseApproveDto {
    Boolean isApproved;
    Long marketplaceId;

    private String string;

    private String stringIdentifier;



}
