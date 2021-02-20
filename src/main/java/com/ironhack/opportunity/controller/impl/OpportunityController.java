package com.ironhack.opportunity.controller.impl;

import com.ironhack.opportunity.controller.DTO.OpportunityDTO;
import com.ironhack.opportunity.controller.interfaces.IOpportunityController;
import com.ironhack.opportunity.service.interfaces.IOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OpportunityController implements IOpportunityController {

    @Autowired
    IOpportunityService opportunityService;

    @GetMapping("/opportunity/{id}")
    public OpportunityDTO getOpportunity(@PathVariable Integer id) {
        return opportunityService.getOpportunity(id);
    }

    @GetMapping("/opportunity/all")
    public List<OpportunityDTO> getAllOpportunities() {
        return opportunityService.getAllOpportunities();
    }

    @PostMapping("/opportunity")
    public OpportunityDTO postOpportunity(@RequestBody OpportunityDTO opportunityDTO) {
        return opportunityService.postOpportunity(opportunityDTO);
    }

    @PutMapping("/opportunity/{id}")
    public OpportunityDTO putOpportunity(@PathVariable Integer id, @RequestBody OpportunityDTO opportunityDTO) {
        return opportunityService.putOpportunity(id, opportunityDTO);
    }

    @DeleteMapping("/opportunity/{id}")
    public boolean deleteOpportunity(@PathVariable Integer id) {
        return opportunityService.deleteOpportunity(id);
    }
}
