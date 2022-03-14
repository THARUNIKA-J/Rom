package com.cts.portal.service;

import com.cts.portal.exception.RequestIdNotFoundException;
import com.cts.portal.exception.TokenInvalidException;
import com.cts.portal.feignClient.AuthFeignClient;
import com.cts.portal.payload.AuthResponsePayload;
import com.cts.portal.payload.ReturnResponsePayload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class PortalService {
    private final AuthFeignClient authFeignClient;

    @Autowired
    public PortalService(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }

    /*
     * Checks the session for the user object is present or not and returns the authResponse.
     * */
    public AuthResponsePayload checkTokenInSession(HttpSession session) {
        try {
            AuthResponsePayload response = (AuthResponsePayload) session.getAttribute("user");
            authFeignClient.validateToken(response.getToken());
            return response;
        } catch (Exception e) {
        	log.error("Token Expired or not Available");
            throw new TokenInvalidException("Token Expired or Not Available");
        }
    }


    /*
     * Checks for the requestId in the ReturnResponsePayload and returns it
     * */
    public String checkForRequestIdInPayload(ReturnResponsePayload response) {
        try {
            return response.getRequestId();
        } catch (Exception e) {
        	log.error("Request Id Not Found");
            throw new RequestIdNotFoundException("Request Id Not Found");
        }
    }
}
