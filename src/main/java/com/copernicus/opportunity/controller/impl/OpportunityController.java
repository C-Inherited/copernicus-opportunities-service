package com.copernicus.opportunity.controller.impl;

import com.copernicus.opportunity.controller.interfaces.IOpportunityController;
import com.copernicus.opportunity.dto.OpportunityDTO;
import com.copernicus.opportunity.service.interfaces.IOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class OpportunityController implements IOpportunityController {

    @Autowired
    IOpportunityService opportunityService;



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

}
