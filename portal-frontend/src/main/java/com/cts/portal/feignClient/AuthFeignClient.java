package com.cts.portal.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.portal.payload.AuthResponsePayload;
import com.cts.portal.payload.UserLoginRequestPayload;

@FeignClient(name = "authentication-service")
public interface AuthFeignClient {
    @PostMapping("/auth/login")
    AuthResponsePayload generateToken(UserLoginRequestPayload userLoginRequestPayload);

    @GetMapping(value = "/auth/validate")
    Boolean validateToken(@RequestHeader("Authorization") String token);

}
