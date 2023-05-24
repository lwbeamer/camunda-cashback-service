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
public class GetCurrentBalance implements JavaDelegate {
    private UserService userService;

    public GetCurrentBalance(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("GetCurrentBalance");

        String username = (String) delegateExecution.getVariable("username");

        User user = userService.getUser(username);

        delegateExecution.removeVariables();
        delegateExecution.setVariable("current-balance", user.getAvailableBalance());
    }
}
