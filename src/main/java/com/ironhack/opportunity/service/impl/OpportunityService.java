package com.ironhack.opportunity.service.impl;

import com.ironhack.opportunity.controller.DTO.OpportunityDTO;
import com.ironhack.opportunity.enums.Product;
import com.ironhack.opportunity.model.Opportunity;
import com.ironhack.opportunity.repository.AccountRepository;
import com.ironhack.opportunity.repository.ContactRepository;
import com.ironhack.opportunity.repository.OpportunityRepository;
import com.ironhack.opportunity.service.interfaces.IOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class OpportunityService implements IOpportunityService {

    @Autowired
    OpportunityRepository opportunityRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    AccountRepository accountRepository;

    public OpportunityDTO getOpportunity(Integer id) {
        if (!opportunityRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Opportunity with ID "+id+" not found");

        OpportunityDTO opportunityDTO = new OpportunityDTO();
        opportunityDTO.parseFromOpportunity(opportunityRepository.getOne(id));

        return opportunityDTO;
    }


    public List<OpportunityDTO> getAllOpportunities() {
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        List<OpportunityDTO> opportunityDTOList = new ArrayList<>();

        for (Opportunity opportunity: opportunityList){
            OpportunityDTO opportunityDTO = new OpportunityDTO();
            opportunityDTOList.add(opportunityDTO.parseFromOpportunity(opportunity));
        }

        return opportunityDTOList;
    }


    public OpportunityDTO postOpportunity(OpportunityDTO opportunityDTO) {
        Opportunity opportunity = new Opportunity(Product.valueOf(opportunityDTO.getProduct()),
                                                  opportunityDTO.getQuantity(),
                                                  contactRepository.findById(opportunityDTO.getContactId()).get(),
                                                  opportunityDTO.getSalesRepId());

        opportunity = opportunityRepository.save(opportunity);
        opportunityDTO.parseFromOpportunity(opportunity);

        return opportunityDTO;
    }


    public OpportunityDTO putOpportunity(Integer id, OpportunityDTO opportunityDTO) {
        Opportunity opportunity = new Opportunity(Product.valueOf(opportunityDTO.getProduct()),
                opportunityDTO.getQuantity(),
                contactRepository.findById(opportunityDTO.getContactId()).get(),
                opportunityDTO.getSalesRepId());
        opportunity.setId(id);
        opportunityDTO.parseFromOpportunity(opportunity);

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

}
