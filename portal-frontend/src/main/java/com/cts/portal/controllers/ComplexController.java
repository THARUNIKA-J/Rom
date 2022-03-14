package com.cts.portal.controllers;

import com.cts.portal.feignClient.AuthFeignClient;
import com.cts.portal.feignClient.ReturnFeignClient;
import com.cts.portal.payload.AuthResponsePayload;
import com.cts.portal.payload.PaymentResponsePayload;
import com.cts.portal.payload.ReturnRequestPayload;
import com.cts.portal.payload.ReturnResponsePayload;
import com.cts.portal.payload.UserLoginRequestPayload;
import com.cts.portal.service.PortalService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;

@Controller
@Slf4j
public class ComplexController {
    private final AuthFeignClient authFeignClient;
    private final ReturnFeignClient returnFeignClient;
    private final PortalService portalService;

    private ReturnResponsePayload returnResponsePayload;

    @Autowired
    public ComplexController(AuthFeignClient authFeignClient, ReturnFeignClient returnFeignClient, PortalService portalService) {
        this.authFeignClient = authFeignClient;
        this.returnFeignClient = returnFeignClient;
        this.portalService = portalService;
    }


    @GetMapping("/payment")
    public String paymentPage(HttpSession session) {
        portalService.checkTokenInSession(session);
        portalService.checkForRequestIdInPayload(returnResponsePayload);
        return "payment.html";
    }

    @GetMapping("/confirmation")
    public String confirmationPage(HttpSession session, Model model) {
        try {
            AuthResponsePayload authResponse = portalService.checkTokenInSession(session);
            portalService.checkForRequestIdInPayload(returnResponsePayload);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            model.addAttribute("userName", authResponse.getUserName());
            model.addAttribute("requestId", returnResponsePayload.getRequestId());
            model.addAttribute("processingCharge", returnResponsePayload.getProcessingCharge());
            model.addAttribute("packageAndDeliveryCharge", returnResponsePayload.getPackageAndDeliveryCharge());
            model.addAttribute("dateOfDelivery", dateFormatter.format(returnResponsePayload.getDateOfDelivery()));
            return "confirmation.html";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @PostMapping("/processLogin")
    public String processLogin(UserLoginRequestPayload userLoginRequestPayload, HttpSession session) {
        try {
            log.info(userLoginRequestPayload.toString());
            AuthResponsePayload authResponsePayload = authFeignClient.generateToken(userLoginRequestPayload);
            session.setAttribute("user", authResponsePayload);
            return "redirect:/";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "redirect:/login?error=true";
        }
    }

    @PostMapping("/processReturnRequest")
    public String processReturnRequest(ReturnRequestPayload returnRequestPayload, HttpSession session) {
        try {
            log.info(returnRequestPayload.toString());
            AuthResponsePayload authResponse = portalService.checkTokenInSession(session);
            returnResponsePayload = returnFeignClient.createReturnRequest(authResponse.getToken(), returnRequestPayload);
            return "redirect:/confirmation";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "redirect:/?retry=true";
        }
    }


    @PostMapping("/processPaymentForRequest")
    public String processPaymentForRequest(@RequestParam long cardNumber, int cvv, HttpSession session) {
        try {
            AuthResponsePayload authResponsePayload = portalService.checkTokenInSession(session);

            PaymentResponsePayload responsePayload = returnFeignClient.makePaymentForReturnRequest(
                    authResponsePayload.getToken(),
                    portalService.checkForRequestIdInPayload(returnResponsePayload),
                    cardNumber,
                    cvv,
                    returnResponsePayload.getProcessingCharge());

            if (responsePayload.getCurrentBalance() <= 0) {
                return "redirect:/payment?insufficientBalance=true";
            }
            invalidateReturnResponse();
            return "success.html";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "failed.html";
        }
    }

    public void invalidateReturnResponse() {
        returnResponsePayload = null;
    }

}
