package com.example.workflow.delegates;

import com.example.workflow.exception.IllegalAccessException;
import com.example.workflow.model.Actor;
import com.example.workflow.model.Role;
import com.example.workflow.security.JwtTokenProvider;
import com.example.workflow.service.ActorService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;


@Named
@Component
public class MarketLogin implements JavaDelegate {


    private final ActorService actorService;
    private final JwtTokenProvider jwtTokenProvider;

    public MarketLogin(ActorService actorService, JwtTokenProvider jwtTokenProvider) {
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

            if (!actor.getRole().equals(Role.MARKET)) throw new IllegalAccessException("У вас нет прав на этот процесс");

            System.out.println(delegateExecution.getCurrentActivityName());

        } catch (Throwable throwable) {
            throw new BpmnError("login_error", throwable.getMessage());
        }
    }
}
