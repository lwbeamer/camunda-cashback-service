package com.example.workflow.delegates;


import com.example.workflow.dto.PurchaseDto;
import com.example.workflow.model.Purchase;
import com.example.workflow.model.User;
import com.example.workflow.service.PurchaseService;
import com.example.workflow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@Component
@Slf4j
public class GetPurchaseHistory implements JavaDelegate {

    private UserService userService;

    private PurchaseService purchaseService;

    public GetPurchaseHistory(UserService userService, PurchaseService purchaseService) {
        this.userService = userService;
        this.purchaseService = purchaseService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("GetPurchaseHistory");

        String username = (String) delegateExecution.getVariable("username");
        int page = Integer.parseInt((String) delegateExecution.getVariable("page"));
        int size = Integer.parseInt((String) delegateExecution.getVariable("size"));

        User user = userService.getUser(username);

        List<PurchaseDto> history = purchaseService.getPurchasePage(user, page, size);

        delegateExecution.removeVariables();

        for (PurchaseDto purchaseDto : history) {
            delegateExecution.setVariable("purchase"+purchaseDto.getId(),purchaseDto.toString());
        }


    }
}
