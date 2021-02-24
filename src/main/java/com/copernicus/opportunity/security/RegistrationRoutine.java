package com.copernicus.opportunity.security;

import com.copernicus.opportunity.clients.AccountClient;
import com.copernicus.opportunity.clients.ContactClient;
import com.copernicus.opportunity.controller.impl.OpportunityController;
import com.copernicus.opportunity.dto.AuthenticationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


@Component
public class RegistrationRoutine {

    @Autowired
    ContactClient contactClient;

    @Autowired
    AccountClient accountClient;

    public static boolean isContactRegistered = false;
    public static boolean isAccountRegistered = false;

    private static final Logger log = LoggerFactory.getLogger(RegistrationRoutine.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private CircuitBreakerFactory circuitBreakerFactory = new Resilience4JCircuitBreakerFactory();

    @Scheduled(fixedRate = 10000)
    public void checkRegistrationContact() {
        if (!isContactRegistered){
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("contact-service");
            log.info("Trying to register with contact-service {}", dateFormat.format(new Date()));
            AuthenticationRequest authenticationRequest = new AuthenticationRequest("opportunity-service", "opportunity-service");
            ResponseEntity<?> responseEntity= circuitBreaker.run(() -> contactClient.createAuthenticationToken(authenticationRequest), throwable -> fallbackTransaction("contact-service"));
            if (responseEntity != null) {
                parseJWT(responseEntity);
                isContactRegistered = true;
                log.info("Registered with contact-service auth token: {}", OpportunityController.getValidationAuthOk());
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    public void checkRegistrationAccount() {
        if (!isAccountRegistered){
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("account-service");
            log.info("Trying to register with account-service {}", dateFormat.format(new Date()));
            AuthenticationRequest authenticationRequest = new AuthenticationRequest("opportunity-service", "opportunity-service");
            ResponseEntity<?> responseEntity= circuitBreaker.run(() -> accountClient.createAuthenticationToken(authenticationRequest), throwable -> fallbackTransaction("account-service"));
            if (responseEntity != null) {
                parseJWT(responseEntity);
                isAccountRegistered = true;
                log.info("Registered with account-service auth token: {}", OpportunityController.getValidationAuthOk());
            }
        }
    }

    private void parseJWT(ResponseEntity<?> responseEntity) {
        String auth = Objects.requireNonNull(responseEntity.getBody()).toString();
        OpportunityController.setValidationAuthOk(auth.substring(5, auth.length() - 1));
    }

    private ResponseEntity<?> fallbackTransaction(String serviceName) {
        log.info( serviceName + " is not reachable {}", dateFormat.format(new Date()));
        return null;
    }
}