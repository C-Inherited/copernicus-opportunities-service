package com.copernicus.opportunity.clients;

import com.copernicus.opportunity.dto.AccountDTO;
import com.copernicus.opportunity.dto.AuthenticationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("account-service")
public interface AccountClient {
    @GetMapping("/account/{id}")
    AccountDTO getAccount(@PathVariable(name = "id") Integer id, @RequestHeader(value = "Authorization") String authorizationHeader);

    @RequestMapping(value = "account/authenticate", method = RequestMethod.POST)
    ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest);
}
