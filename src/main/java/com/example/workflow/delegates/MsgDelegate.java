package com.example.workflow.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class MsgDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("msg_01")
//                .processInstanceBusinessKey(delegateExecution.getProcessBusinessKey())
                .setVariable("first", "smth1")
                .setVariable("second", "smth2")
                .correlate();
    }
}
