package com.example.workflow.service;//package com.business.app.service;

import bitronix.tm.BitronixTransactionManager;
import com.example.workflow.exception.NotHandledPurchaseException;
import com.example.workflow.exception.TransactionException;
import com.example.workflow.util.IdGenerator;
import com.example.workflow.dto.PurchaseDto;
import com.example.workflow.dto.PurchaseApproveDto;
import com.example.workflow.dto.PurchaseFromMarketplaceDto;
import com.example.workflow.exception.IllegalPageParametersException;
import com.example.workflow.exception.ResourceNotFoundException;
import com.example.workflow.model.*;
import com.example.workflow.repository.PurchaseRepository;
import com.example.workflow.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.SystemException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PurchaseService {
    private final RedirectService redirectService;

    private final PurchaseRepository purchaseRepository;

    private final UserRepository userRepository;
    private final BitronixTransactionManager transactionManager;



    private RestTemplate restTemplate;


    private final ObjectMapper mapper;


    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
    }

    public PurchaseService(RedirectService redirectService, PurchaseRepository purchaseRepository, UserRepository userRepository, BitronixTransactionManager transactionManager, ObjectMapper mapper) {
        this.redirectService = redirectService;
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.transactionManager = transactionManager;
        this.mapper = mapper;
    }

    private boolean checkRules(Rules rules, PurchaseFromMarketplaceDto purchase) {
        return rules.getMinPrice() <= purchase.getTotalPrice();
    }

    private boolean checkTimeDeadline(long started) {
        return (Timestamp.from(Instant.now()).getTime() - started) / 1000 <= 300;
    }

    public Purchase purchaseAdd(PurchaseFromMarketplaceDto purchaseFromMarketplaceDto) throws SystemException {
        Purchase purchase = new Purchase();
        Redirect redirect = redirectService.getRedirect(purchaseFromMarketplaceDto.getUsername(), purchaseFromMarketplaceDto.getMarketplaceId());
        purchase.setStringIdentifier(purchaseFromMarketplaceDto.getStringIdentifier());
        purchase.setUser(redirect.getPk().getUser());
        purchase.setMarketplace(redirect.getPk().getMarketplace());
        purchase.setTimestamp(Timestamp.from(Instant.now()));
        purchase.setCashbackPercent(purchaseFromMarketplaceDto.getCashbackPercent());
        purchase.setTotalPrice(purchaseFromMarketplaceDto.getTotalPrice());
        try {
            transactionManager.begin();

            if (checkTimeDeadline(redirect.getTime().getTime())) {
                User user = purchase.getUser();
                if (checkRules(redirect.getPk().getMarketplace().getRules(), purchaseFromMarketplaceDto)) {
                    purchase.setCashbackPaymentStatus(Status.PENDING);
                    purchase.setRulesRespected(true);
                    user.setPendingBalance(user.getPendingBalance() + purchase.getCashbackPercent() * purchase.getTotalPrice() / 100);
                } else {
                    purchase.setCashbackPaymentStatus(Status.REJECTED);
                    purchase.setRulesRespected(false);
                }
                userRepository.save(user);
                purchaseRepository.save(purchase);
                redirectService.removeRedirect(redirect);
                log.info("Покупка совершена успешно: "+purchaseFromMarketplaceDto.getStringIdentifier());
                transactionManager.commit();
                return purchase;
            } else {
                throw new NotHandledPurchaseException("Not handled purchase because of time limit");
            }
        } catch (Exception e) {
            transactionManager.rollback();
            purchase.setCashbackPaymentStatus(Status.REJECTED);
            purchase.setErrorMessage(e.getMessage());
            purchaseRepository.save(purchase);
            throw new TransactionException("Ошибка выполнения транзакции: " + e.getMessage());
        }
    }

    public void approvePurchase(PurchaseApproveDto purchaseApproveDto) throws SystemException {
        try {
            transactionManager.begin();
            Purchase purchase = purchaseRepository.findByStringIdentifier(purchaseApproveDto.getStringIdentifier()).orElse(null);
            if (purchase != null) {
                if (purchase.getErrorMessage() != null) throw new NotHandledPurchaseException("This purchase has errors and couldn't be approved!");
                if (purchase.getMarketplace().getId().equals(purchaseApproveDto.getMarketplaceId())) {
                    if (purchase.isRulesRespected() && purchase.getCashbackPaymentStatus() == Status.PENDING) {
                        User user = purchase.getUser();
                        if (purchaseApproveDto.getIsApproved()) {
                            user.setAvailableBalance(user.getAvailableBalance() + purchase.getTotalPrice() * purchase.getCashbackPercent() / 100);
                            purchase.setCashbackPaymentStatus(Status.APPROVED);
                        } else {
                            purchase.setCashbackPaymentStatus(Status.REJECTED);
                        }
                        user.setPendingBalance(user.getPendingBalance() - purchase.getTotalPrice() * purchase.getCashbackPercent() / 100);
                        purchase.setErrorMessage(null);
                        userRepository.save(user);
                        purchaseRepository.save(purchase);
                        log.info("Покупка подтверждена успешно: "+purchaseApproveDto.getStringIdentifier());
                        transactionManager.commit();
                    } else {
                        throw new NotHandledPurchaseException("Cannot handle with rejected or approved purchase!");
                    }
                } else {
                    throw new NotHandledPurchaseException("Market that make request is not same as purchase market!");
                }
            } else {
                throw new NotHandledPurchaseException("Cannot find purchase with this id");
            }
        } catch (Exception e) {
            transactionManager.rollback();
            throw new TransactionException("Ошибка выполнения транзакции: " + e.getMessage());
        }
    }


    public List<PurchaseDto> getPurchasePage(User user, int pageNum, int pageSize) {

        if (pageNum < 1 || pageSize < 1)
            throw new IllegalPageParametersException("Номер страницы и её размер должны быть больше 1");

        Pageable pageRequest = createPageRequest(pageNum - 1, pageSize);

        Page<Purchase> resultPage = purchaseRepository.findAllByUser(user, pageRequest);

        if (resultPage.getTotalPages() < pageNum)
            throw new ResourceNotFoundException("На указанной странице не найдено записей!");

        List<PurchaseDto> resultList = new ArrayList<>();

        for (Purchase purchase : resultPage.getContent()) {
            resultList.add(PurchaseDto.fromPurchase(purchase));
        }

        return resultList;

    }


    private Pageable createPageRequest(int pageNum, int pageSize) {
        return PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "timestamp");
    }


}
