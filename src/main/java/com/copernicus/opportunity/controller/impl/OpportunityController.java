package com.copernicus.opportunity.controller.impl;

import com.copernicus.opportunity.controller.interfaces.IOpportunityController;
import com.copernicus.opportunity.dto.AuthenticationRequest;
import com.copernicus.opportunity.dto.AuthenticationResponse;
import com.copernicus.opportunity.dto.OpportunityDTO;
import com.copernicus.opportunity.security.MyUserDetailsService;
import com.copernicus.opportunity.service.interfaces.IOpportunityService;
import com.copernicus.opportunity.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class OpportunityController implements IOpportunityController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    IOpportunityService opportunityService;

    private static String accountAuthOk;
    private static String contactAuthOk;


    @GetMapping("/opportunity/{id}")
    public OpportunityDTO getOpportunity(@PathVariable Integer id, @RequestHeader(value = "Authorization") String authorizationHeader) {
        return opportunityService.getOpportunity(id);
    }

    @GetMapping("/opportunity/all")
    public List<OpportunityDTO> getAllOpportunities(@RequestHeader(value = "Authorization") String authorizationHeader) {
        return opportunityService.getAllOpportunities();
    }

    @PostMapping("/opportunity")
    public OpportunityDTO postOpportunity(@RequestBody OpportunityDTO opportunityDTO, @RequestHeader(value = "Authorization") String authorizationHeader) {
        return opportunityService.postOpportunity(opportunityDTO);
    }

    @PutMapping("/opportunity/{id}")
    public OpportunityDTO putOpportunity(@PathVariable Integer id, @RequestBody OpportunityDTO opportunityDTO, @RequestHeader(value = "Authorization") String authorizationHeader) {
        return opportunityService.putOpportunity(id, opportunityDTO);
    }
    
    @GetMapping("/opportunity/all/{salesRepId}")
    public List<OpportunityDTO> findOpportunitiesBySalesRep(@PathVariable Integer salesRepId, @RequestParam Optional<String> status, @RequestHeader(value = "Authorization") String authorizationHeader) {
        return opportunityService.findOpportunitiesBySalesRep(salesRepId, status);
    }

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
        OpportunityController.accountAuthOk = accountAuthOk;
    }

    public static String getContactAuthOk() {
        return contactAuthOk;
    }

    public static void setContactAuthOk(String contactAuthOk) {
        OpportunityController.contactAuthOk = contactAuthOk;
    }

}
