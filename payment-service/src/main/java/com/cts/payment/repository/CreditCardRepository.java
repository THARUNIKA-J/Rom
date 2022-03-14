package com.cts.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.cts.payment.entity.CreditCard;
import com.cts.payment.exception.CardNotFoundException;

@Component
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    CreditCard findByCardNumber(long cardNumber) throws CardNotFoundException;
}
