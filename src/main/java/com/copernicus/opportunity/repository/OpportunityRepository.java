package com.copernicus.opportunity.repository;

import com.copernicus.opportunity.enums.Status;
import com.copernicus.opportunity.model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {


    List<Opportunity> getOpportunityBySalesRepId(Integer salesRepId);

    List<Opportunity> getOpportunityBySalesRepIdAndStatus(Integer salesRepId, Status status);
}
