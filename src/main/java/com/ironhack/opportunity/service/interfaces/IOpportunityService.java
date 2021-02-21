package com.ironhack.opportunity.service.interfaces;

import com.ironhack.opportunity.controller.DTO.OpportunityDTO;

import java.util.List;

public interface IOpportunityService {
    OpportunityDTO getOpportunity(Integer id);

    List<OpportunityDTO> getAllOpportunities();

    OpportunityDTO postOpportunity(OpportunityDTO opportunityDTO);

    OpportunityDTO putOpportunity(Integer id, OpportunityDTO opportunityDTO);

    boolean deleteOpportunity(Integer id);

}
