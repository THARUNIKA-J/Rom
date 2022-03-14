package com.cts.componentprocessing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.componentprocessing.entity.ReturnRequest;

@Repository
public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Integer> {
}
