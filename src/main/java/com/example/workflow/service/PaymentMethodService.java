package com.example.workflow.service;

import com.example.workflow.exception.NotFoundPaymentMethodException;
import com.example.workflow.exception.NotFoundUserException;
import com.example.workflow.model.PaymentMethod;
import com.example.workflow.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodService {


    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    public PaymentMethod getPaymentMethod(Long id) throws NotFoundUserException {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id).orElse(null);
        if (paymentMethod != null) {
            return paymentMethod;
        } else {
            throw new NotFoundPaymentMethodException("Способ платежа с таким id не найден!");
        }
    }


}
