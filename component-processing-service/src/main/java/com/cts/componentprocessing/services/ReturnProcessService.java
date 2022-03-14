package com.cts.componentprocessing.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.componentprocessing.clients.PackagingAndDeliveryFeignClient;
import com.cts.componentprocessing.clients.PaymentFeignClient;
import com.cts.componentprocessing.entity.PaymentReturn;
import com.cts.componentprocessing.entity.ReturnRequest;
import com.cts.componentprocessing.payload.PaymentResponse;
import com.cts.componentprocessing.payload.ReturnRequestPayload;
import com.cts.componentprocessing.payload.ReturnResponsePayload;
import com.cts.componentprocessing.repositories.PaymentReturnRepository;
import com.cts.componentprocessing.repositories.ReturnRequestRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Service
@Slf4j
public class ReturnProcessService {
    private final ReturnRequestRepository returnRequestRepository;
    private final PaymentReturnRepository paymentReturnRepository;
    private final PaymentFeignClient paymentFeignClient;
    private final PackagingAndDeliveryFeignClient packagingAndDeliveryFeignClient;

    @Autowired
    public ReturnProcessService(
            ReturnRequestRepository returnRequestRepository,
            PaymentReturnRepository paymentReturnRepository,
            PaymentFeignClient paymentFeignClient,
            PackagingAndDeliveryFeignClient packagingAndDeliveryFeignClient) {
        this.returnRequestRepository = returnRequestRepository;
        this.paymentReturnRepository = paymentReturnRepository;
        this.paymentFeignClient = paymentFeignClient;
        this.packagingAndDeliveryFeignClient = packagingAndDeliveryFeignClient;
    }

    public ReturnResponsePayload processReturnRequest(ReturnRequestPayload returnRequestPayload) {
        ReturnRequest returnRequest = new ReturnRequest();
        ReturnResponsePayload returnResponsePayload = new ReturnResponsePayload();

        BeanUtils.copyProperties(returnRequestPayload, returnRequest);

        int processingDays = 5;
        double processingCharge = returnRequestPayload.getComponentType().equalsIgnoreCase("integral") ? 500 : 300;

        if (returnRequestPayload.getComponentType().equalsIgnoreCase("integral") && returnRequestPayload.isPriorityRequest()) {
            processingDays = 2;
            processingCharge += 200;
        }
        LocalDate date = LocalDate.now().plusDays(processingDays);
        returnRequest.setProcessingCharge(processingCharge);
        returnRequest.setDateOfDelivery(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        returnRequest.setRequestId(UUID.randomUUID().toString().replace("-", ""));

        returnRequest.setPackageAndDeliveryCharge(packagingAndDeliveryFeignClient.getPackagingAndDeliveryCharge(
                returnRequestPayload.getComponentType(), returnRequestPayload.getQuantity())
        );

        returnRequestRepository.save(returnRequest);

        BeanUtils.copyProperties(returnRequest, returnResponsePayload);

        return returnResponsePayload;

    }

    public PaymentResponse makePayment(String requestId, long cardNumber, int cvv, double processingCharge) {
        PaymentReturn paymentRequest = new PaymentReturn(requestId, cardNumber, processingCharge);

        paymentReturnRepository.save(paymentRequest);

        log.info("Works here");

        PaymentResponse paymentResponse = paymentFeignClient.getCurrentBalance(cardNumber, cvv, processingCharge);
        log.info("Crashed here");
        log.info(paymentResponse.toString());

//        if (paymentResponse.getCurrentBalance() <= -1)
//            throw new InsufficientBalanceException();
//        else
        return paymentResponse;
    }
}
