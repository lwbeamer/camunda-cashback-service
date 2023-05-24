package com.example.workflow.delegates;

import com.example.workflow.model.User;
import com.example.workflow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Named
@Component
@Slf4j
public class GetPendingBalance implements JavaDelegate {

    private UserService userService;

    public GetPendingBalance(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("GetPendingBalance");

        String username = (String) delegateExecution.getVariable("username");

        User user = userService.getUser(username);

        delegateExecution.removeVariables();
        delegateExecution.setVariable("pending-balance", user.getPendingBalance());


    }
}
