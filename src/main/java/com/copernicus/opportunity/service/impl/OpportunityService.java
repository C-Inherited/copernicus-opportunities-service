package com.copernicus.opportunity.service.impl;

import com.copernicus.opportunity.clients.AccountClient;
import com.copernicus.opportunity.clients.ContactClient;
import com.copernicus.opportunity.controller.impl.OpportunityController;
import com.copernicus.opportunity.dto.AccountDTO;
import com.copernicus.opportunity.dto.ContactDTO;
import com.copernicus.opportunity.dto.OpportunityDTO;
import com.copernicus.opportunity.enums.Product;
import com.copernicus.opportunity.enums.Status;
import com.copernicus.opportunity.model.Account;
import com.copernicus.opportunity.model.Contact;
import com.copernicus.opportunity.model.Opportunity;
import com.copernicus.opportunity.repository.AccountRepository;
import com.copernicus.opportunity.repository.ContactRepository;
import com.copernicus.opportunity.repository.OpportunityRepository;
import com.copernicus.opportunity.service.interfaces.IOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class OpportunityService implements IOpportunityService {

    @Autowired
    OpportunityRepository opportunityRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    AccountClient accountClient;
    @Autowired
    ContactClient contactClient;

    private final CircuitBreakerFactory circuitBreakerFactory = new Resilience4JCircuitBreakerFactory();

    public OpportunityDTO getOpportunity(Integer id) {
        if (!opportunityRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Opportunity with ID "+id+" not found");

        OpportunityDTO opportunityDTO = new OpportunityDTO();
        opportunityDTO = OpportunityDTO.parseFromOpportunity(opportunityRepository.getOne(id));

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
        Opportunity opportunity = createOpportunityFromDTO(opportunityDTO);
        opportunity = opportunityRepository.save(opportunity);
        opportunityDTO = OpportunityDTO.parseFromOpportunity(opportunity);

        return opportunityDTO;
    }


    public OpportunityDTO putOpportunity(Integer id, OpportunityDTO opportunityDTO) {
        if (!opportunityRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Opportunity with ID "+id+" not found");

        Opportunity opportunity = createOpportunityFromDTO(opportunityDTO);
        opportunity.setId(id);
        OpportunityDTO.parseFromOpportunity(opportunity);

        opportunityRepository.save(opportunity);

        return opportunityDTO;
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


    private Opportunity createOpportunityFromDTO(OpportunityDTO opportunityDTO){
        CircuitBreaker accountCircuit = circuitBreakerFactory.create("account-service");
        CircuitBreaker contactCircuit = circuitBreakerFactory.create("contact-service");

        AccountDTO accountDTO = accountCircuit.run(() -> accountClient.getAccount(opportunityDTO.getAccountId(), "Bearer "+ OpportunityController.getAccountAuthOk()),
                                                   throwable -> accountFallback(opportunityDTO.getAccountId()));
        ContactDTO contactDTO = contactCircuit.run(() -> contactClient.getContact(opportunityDTO.getContactId(), "Bearer "+ OpportunityController.getContactAuthOk()),
                                                   throwable -> contactFallback(opportunityDTO.getContactId()));

        Account account = Account.parseFromDTO(accountDTO);

        Contact contact = new Contact(contactDTO.getName(),
                                      contactDTO.getPhoneNumber(),
                                      contactDTO.getEmail(),
                                      contactDTO.getCompanyName(),
                                      account);

        try {
            Product.valueOf(opportunityDTO.getProduct());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid product type. Use BOX, HYBRID or FLATBED");
        }

        Opportunity opportunity = new Opportunity(Product.valueOf(opportunityDTO.getProduct()),
                opportunityDTO.getQuantity(),
                contact,
                opportunityDTO.getSalesRepId(),
                account);
        return opportunity;
    }

    private AccountDTO accountFallback(Integer id){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id "+id+" not found");
    }

    private ContactDTO contactFallback(Integer id){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact with id "+id+" not found");
    }

}
