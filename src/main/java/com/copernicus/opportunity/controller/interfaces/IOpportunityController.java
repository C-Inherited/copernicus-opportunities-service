package com.copernicus.opportunity.controller.interfaces;

import com.copernicus.opportunity.dto.OpportunityDTO;

import java.util.List;
import java.util.Optional;

public interface IOpportunityController {
    OpportunityDTO getOpportunity(Integer id, String authorizationHeader);

    List<OpportunityDTO> getAllOpportunities(String authorizationHeader);

    OpportunityDTO postOpportunity(OpportunityDTO opportunityDTO, String authorizationHeader);

    OpportunityDTO putOpportunity(Integer id, OpportunityDTO opportunityDTO, String authorizationHeader);

    List<OpportunityDTO> findOpportunitiesBySalesRep(Integer salesRepId, Optional<String> status, String authorizationHeader);
}
