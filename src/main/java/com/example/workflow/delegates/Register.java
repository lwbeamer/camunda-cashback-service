package com.example.workflow.delegates;



import com.example.workflow.model.User;
import com.example.workflow.service.ActorService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Component
@Named
public class Register implements JavaDelegate {
    private final ActorService actorService;

    public Register(ActorService actorService) {
        this.actorService = actorService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            String username = (String) delegateExecution.getVariable("username");
            String password = (String) delegateExecution.getVariable("password");
            User user = actorService.register(username, password);
        }
        catch(Throwable throwable){
            throw new BpmnError("register_error", throwable.getMessage());
        }
    }
}
