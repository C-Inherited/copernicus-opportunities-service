package com.ironhack.opportunity.controller.interfaces;

import com.ironhack.opportunity.controller.DTO.OpportunityDTO;

import java.util.List;

public interface IOpportunityController {
    OpportunityDTO getOpportunity(Integer id);

    List<OpportunityDTO> getAllOpportunities();

    OpportunityDTO postOpportunity(OpportunityDTO opportunityDTO);

    OpportunityDTO putOpportunity(Integer id, OpportunityDTO opportunityDTO);

    boolean deleteOpportunity(Integer id);

}
