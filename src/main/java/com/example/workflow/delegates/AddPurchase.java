package com.example.workflow.delegates;

import com.example.workflow.dto.PurchaseFromMarketplaceDto;
import com.example.workflow.model.Purchase;
import com.example.workflow.service.PurchaseService;
import com.example.workflow.util.IdGenerator;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Component
@Named
public class AddPurchase implements JavaDelegate {
    final PurchaseService purchaseService;

    public AddPurchase(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        PurchaseFromMarketplaceDto purchase = new PurchaseFromMarketplaceDto();
        purchase.setUsername((String) delegateExecution.getVariable("user_username"));
        purchase.setMarketplaceId((Integer) delegateExecution.getVariable("marketplaceId"));
        purchase.setCashbackPercent((Integer) delegateExecution.getVariable("cashbackPercent"));
        purchase.setTotalPrice((Integer) delegateExecution.getVariable("totalPrice"));
        purchase.setStringIdentifier(IdGenerator.generateId());

        delegateExecution.setVariable("add-purchase-string-id",purchase.getStringIdentifier());


        try {
            Purchase p = purchaseService.purchaseAdd(purchase);
            delegateExecution.setVariable("rulesRespected", p.isRulesRespected());
            delegateExecution.removeVariable("password");
        } catch (RuntimeException e){
            throw new BpmnError("add-purchase-error",e.getMessage());
        }
    }
}
