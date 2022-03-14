package com.cts.componentprocessing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.componentprocessing.entity.PaymentReturn;

@Repository
public interface PaymentReturnRepository extends JpaRepository<PaymentReturn, Integer> {
}
