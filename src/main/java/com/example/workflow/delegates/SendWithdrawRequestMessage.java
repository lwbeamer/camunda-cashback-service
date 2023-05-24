package com.example.workflow.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class SendWithdrawRequestMessage implements JavaDelegate {


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println(delegateExecution.getCurrentActivityName());

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("withdraw-request")
                .setVariable("withdraw-string-id",delegateExecution.getVariable("withdraw-string-id"))
                .correlate();
    }
}
