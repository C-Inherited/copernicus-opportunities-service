package com.copernicus.opportunity.clients;

import com.copernicus.opportunity.auth.dto.AuthenticationRequest;
import com.copernicus.opportunity.dto.ContactDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("contact-service")
public interface ContactClient {
    @GetMapping("/contact/{id}")
    ContactDTO getContact(@PathVariable Integer id, @RequestHeader(value = "Authorization") String authorizationHeader);

    @RequestMapping(value = "contact/authenticate", method = RequestMethod.POST)
    ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest);
}
