package com.example.workflow.delegates;



import com.example.workflow.model.Actor;
import com.example.workflow.security.JwtTokenProvider;
import com.example.workflow.service.ActorService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Component
@Named
public class Login implements JavaDelegate {
    private final ActorService actorService;
    private final JwtTokenProvider jwtTokenProvider;

    public Login(ActorService actorService, JwtTokenProvider jwtTokenProvider) {
        this.actorService = actorService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            String username = (String) delegateExecution.getVariable("username");
            String password = (String) delegateExecution.getVariable("password");

            Actor actor = null;
            actor = actorService.getActor(username.trim());
            String accessToken = jwtTokenProvider.createAccessToken(username.trim(), actor.getRole().name());
            String refreshToken = jwtTokenProvider.createRefreshToken(username.trim(), actor.getRole().name());

            delegateExecution.setVariable("accessToken", accessToken);
            delegateExecution.setVariable("refreshToken", refreshToken);

            System.out.println(delegateExecution.getCurrentActivityName());


        } catch (Throwable throwable) {
            throw new BpmnError("login_error", throwable.getMessage());
        }
    }
}
