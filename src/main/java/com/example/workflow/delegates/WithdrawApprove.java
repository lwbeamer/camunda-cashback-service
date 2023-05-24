package com.example.workflow.delegates;

import com.example.workflow.dto.WithdrawApproveDto;
import com.example.workflow.service.WithdrawService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.Random;

@Component
@Named
public class WithdrawApprove implements JavaDelegate {

    private WithdrawService withdrawService;

    public WithdrawApprove(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {


        WithdrawApproveDto withdrawApproveDto = new WithdrawApproveDto();


        withdrawApproveDto.setStringIdentifier((String) delegateExecution.getVariable("withdraw-string-id"));
        withdrawApproveDto.setIsApproved(new Random().nextBoolean());


        try {
            withdrawService.approveWithdraw(withdrawApproveDto);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            throw new BpmnError("withdraw-approve-error",e.getMessage());
        }

        System.out.println(delegateExecution.getCurrentActivityName());


    }
}
