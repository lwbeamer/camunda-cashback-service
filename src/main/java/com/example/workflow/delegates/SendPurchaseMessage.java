package com.example.workflow.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Component
@Named
public class SendPurchaseMessage implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println(delegateExecution.getCurrentActivityName());

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("purchase-msg")
                .setVariable("purchase-string-id",delegateExecution.getVariable("purchase-string-id"))
                .setVariable("marketplaceId", delegateExecution.getVariable("marketplaceId"))
                .correlate();
    }
}
