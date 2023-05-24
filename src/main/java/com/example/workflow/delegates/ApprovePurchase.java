package com.example.workflow.delegates;

import com.example.workflow.dto.PurchaseApproveDto;
import com.example.workflow.service.PurchaseService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.Random;

@Component
@Named
public class ApprovePurchase implements JavaDelegate {
    final PurchaseService purchaseService;

    public ApprovePurchase(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        PurchaseApproveDto purchaseApproveDto = new PurchaseApproveDto();

        purchaseApproveDto.setStringIdentifier((String) delegateExecution.getVariable("purchase-string-id"));
        purchaseApproveDto.setIsApproved(new Random().nextBoolean());
        purchaseApproveDto.setMarketplaceId(Long.valueOf((Integer) delegateExecution.getVariable("marketplaceId")));

        try {
            purchaseService.approvePurchase(purchaseApproveDto);
        }catch (RuntimeException runtimeException){
            throw new BpmnError(runtimeException.getMessage());
        }
    }
}
