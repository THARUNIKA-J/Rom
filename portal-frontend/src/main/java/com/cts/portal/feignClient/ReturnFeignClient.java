package com.cts.portal.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.portal.payload.PaymentResponsePayload;
import com.cts.portal.payload.ReturnRequestPayload;
import com.cts.portal.payload.ReturnResponsePayload;

@FeignClient(name = "component-processing-service")
public interface ReturnFeignClient {
    @PostMapping("/returns/createReturnRequest")
    ReturnResponsePayload createReturnRequest(
            @RequestHeader("Authorization") String token,
            ReturnRequestPayload returnRequestPayload
    );

    @PostMapping("/returns/paymentForReturn/{requestId}/{cardNumber}/{cvv}/{processingCharge}")
    PaymentResponsePayload makePaymentForReturnRequest(@RequestHeader("Authorization") String token, @PathVariable String requestId,
                                                       @PathVariable long cardNumber,
                                                       @PathVariable int cvv,
                                                       @PathVariable double processingCharge);

}
