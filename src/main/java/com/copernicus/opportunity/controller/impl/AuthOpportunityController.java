package com.copernicus.opportunity.controller.impl;

import com.copernicus.opportunity.auth.dto.AuthenticationRequest;
import com.copernicus.opportunity.auth.dto.AuthenticationResponse;
import com.copernicus.opportunity.auth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.copernicus.opportunity.auth.security.MyUserDetailsService;

@RestController
public class AuthOpportunityController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    private static String accountAuthOk;
    private static String contactAuthOk;



    @RequestMapping(value = "/opportunity/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


    public static String getAccountAuthOk() {
        return accountAuthOk;
    }

    public static void setAccountAuthOk(String accountAuthOk) {
        AuthOpportunityController.accountAuthOk = accountAuthOk;
    }

    public static String getContactAuthOk() {
        return contactAuthOk;
    }

    public static void setContactAuthOk(String contactAuthOk) {
        AuthOpportunityController.contactAuthOk = contactAuthOk;
    }

}
