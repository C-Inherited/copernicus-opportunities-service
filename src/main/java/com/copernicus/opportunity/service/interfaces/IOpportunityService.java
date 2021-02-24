package com.copernicus.opportunity.service.interfaces;

import com.copernicus.opportunity.dto.OpportunityDTO;
import com.copernicus.opportunity.model.Opportunity;

import java.util.List;
import java.util.Optional;

public interface IOpportunityService {
    OpportunityDTO getOpportunity(Integer id);

    List<OpportunityDTO> getAllOpportunities();

    OpportunityDTO postOpportunity(OpportunityDTO opportunityDTO);

    OpportunityDTO putOpportunity(Integer id, OpportunityDTO opportunityDTO);

    List<OpportunityDTO> findOpportunitiesBySalesRep(Integer salesRepId, Optional<String> status);
}
