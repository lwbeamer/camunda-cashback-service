package com.example.workflow.delegates;

import com.example.workflow.dto.RedirectDto;
import com.example.workflow.service.RedirectService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Component
@Named
public class Redirect implements JavaDelegate {
    final RedirectService redirectService;

    public Redirect(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        RedirectDto redirectDto = new RedirectDto();
        redirectDto.setMarketplaceId(Long.valueOf((Integer)delegateExecution.getVariable("marketplaceId")));
        redirectDto.setUserId((String) delegateExecution.getVariable("username"));

        try {
            redirectService.addRedirect(redirectDto);
            delegateExecution.setVariable("user_username", redirectDto.getUserId());
            delegateExecution.removeVariable("password");
        } catch (RuntimeException e){
            throw new BpmnError("redirect-error",e.getMessage());
        }
    }
}
