package com.example.workflow.delegates;

import com.example.workflow.dto.WithdrawDto;
import com.example.workflow.service.WithdrawService;
import com.example.workflow.util.IdGenerator;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Named
@Component
public class WithdrawRequest implements JavaDelegate {

    private WithdrawService withdrawService;

    public WithdrawRequest(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {


        WithdrawDto withdrawDto = new WithdrawDto();

        withdrawDto.setAmount(Double.parseDouble((String) delegateExecution.getVariable("withdraw-amount")));
        withdrawDto.setCredential((String) delegateExecution.getVariable("credential"));
        withdrawDto.setPaymentMethodId(Long.parseLong((String) delegateExecution.getVariable("payment-method-id")));
        withdrawDto.setUsername((String) delegateExecution.getVariable("username"));
        withdrawDto.setStringIdentifier(IdGenerator.generateId());

        delegateExecution.setVariable("withdraw-string-id",withdrawDto.getStringIdentifier());


        try {
            withdrawService.sendWithdraw(withdrawDto);
        } catch (RuntimeException e){
            throw new BpmnError("withdrawRequestError",e.getMessage());
        }
        System.out.println(delegateExecution.getCurrentActivityName());


    }
}
