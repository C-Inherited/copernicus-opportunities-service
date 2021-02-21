package com.copernicus.opportunity.service.impl;

import com.copernicus.opportunity.dto.OpportunityDTO;
import com.copernicus.opportunity.dto.RequestDTO;
import com.copernicus.opportunity.enums.*;
import com.copernicus.opportunity.model.Opportunity;
import com.copernicus.opportunity.repository.AccountRepository;
import com.copernicus.opportunity.repository.ContactRepository;
import com.copernicus.opportunity.repository.OpportunityRepository;
import com.copernicus.opportunity.service.interfaces.IOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OpportunityService implements IOpportunityService {

    @Autowired
    OpportunityRepository opportunityRepository;
    @Autowired
    ContactRepository contactRepository;

    public OpportunityDTO getOpportunity(Integer id) {
        if (!opportunityRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Opportunity with ID "+id+" not found");

        OpportunityDTO opportunityDTO = new OpportunityDTO();
        OpportunityDTO.parseFromOpportunity(opportunityRepository.getOne(id));

        return opportunityDTO;
    }


    public List<OpportunityDTO> getAllOpportunities() {
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        List<OpportunityDTO> opportunityDTOList = new ArrayList<>();

        for (Opportunity opportunity: opportunityList){
            OpportunityDTO opportunityDTO = new OpportunityDTO();
            opportunityDTOList.add(OpportunityDTO.parseFromOpportunity(opportunity));
        }

        return opportunityDTOList;
    }


    public OpportunityDTO postOpportunity(OpportunityDTO opportunityDTO) {
        Opportunity opportunity = new Opportunity(Product.valueOf(opportunityDTO.getProduct()),
                                                  opportunityDTO.getQuantity(),
                                                  contactRepository.findById(opportunityDTO.getContactId()).get(),
                                                  opportunityDTO.getSalesRepId());

        opportunity = opportunityRepository.save(opportunity);
        OpportunityDTO.parseFromOpportunity(opportunity);

        return opportunityDTO;
    }


    public OpportunityDTO putOpportunity(Integer id, OpportunityDTO opportunityDTO) {
        if (!opportunityRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Opportunity with ID "+id+" not found");

        Opportunity opportunity = new Opportunity(Product.valueOf(opportunityDTO.getProduct()),
                opportunityDTO.getQuantity(),
                contactRepository.findById(opportunityDTO.getContactId()).get(),
                opportunityDTO.getSalesRepId());
        opportunity.setId(id);
        OpportunityDTO.parseFromOpportunity(opportunity);

        opportunityRepository.save(opportunity);

        return opportunityDTO;
    }


    public boolean deleteOpportunity(Integer id) {
        try{
            opportunityRepository.deleteById(id);
        }catch(Exception e){
            return false;
        }
        return true;
    }


    public List<OpportunityDTO> findOpportunitiesBySalesRep(Integer salesRepId, Optional<String> status) {
        if (status.isEmpty()){
            return opportunityRepository.getOpportunityBySalesRepId(salesRepId).stream()
                    .map(OpportunityDTO::parseFromOpportunity).collect(Collectors.toList());
        }else{
            return opportunityRepository.getOpportunityBySalesRepIdAndStatus(salesRepId, Status.valueOf(status.get())).stream()
                    .map(OpportunityDTO::parseFromOpportunity).collect(Collectors.toList());
        }
    }

    public Integer patchStatusOpportunity(RequestDTO requestDTO) {
        if (!opportunityRepository.existsById(requestDTO.getId())){
            return 0;
        }

        Opportunity opportunity = opportunityRepository.getOne(requestDTO.getId());
        opportunity.setStatus(Product.valueOf(requestDTO.getStatus()));
        opportunityRepository.save(opportunity);

        if (opportunity.getStatus().equals(Status.CLOSED_LOST)){
            return -1;
        }
        if (opportunity.getStatus().equals(Status.CLOSED_WON)){
            return 1;
        }

        return 0;

    }


}
