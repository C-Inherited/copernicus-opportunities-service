package com.copernicus.opportunity.service.impl;

import com.copernicus.opportunity.dto.OpportunityDTO;
import com.copernicus.opportunity.enums.Industry;
import com.copernicus.opportunity.enums.Product;
import com.copernicus.opportunity.enums.Status;
import com.copernicus.opportunity.model.Account;
import com.copernicus.opportunity.model.Contact;
import com.copernicus.opportunity.model.Opportunity;
import com.copernicus.opportunity.repository.AccountRepository;
import com.copernicus.opportunity.repository.ContactRepository;
import com.copernicus.opportunity.repository.OpportunityRepository;
import com.copernicus.opportunity.service.interfaces.IOpportunityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OpportunityServiceTest {

    @Autowired
    OpportunityRepository opportunityRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    IOpportunityService opportunityService;

    Optional<String> status1 = Optional.empty();
    Optional<String> status2 = Optional.of("OPEN");


    @BeforeEach
    void setUp() {

        Account account1 = new Account(Industry.OTHER, 40, "Albacete", "ESPAÑA");
        Account account2 = new Account(Industry.MEDICAL, 75, "Buguibugui", "EZPAÑA");
        accountRepository.saveAll(List.of(account1,account2));

        Contact contact1 = new Contact("Pepa Pig", "676767676", "pepa@pig.pp", "Pigs", account1);
        Contact contact2 = new Contact("Ana Cardo", "656565656", "ana@car.do", "Cards", account1);
        Contact contact3 = new Contact("Hula Hop", "656565656", "hu@la.hop", "Huli", account2);

        Opportunity opportunity1 = new Opportunity(Product.BOX, 40, contact1, 1, account1);
        Opportunity opportunity2 = new Opportunity(Product.FLATBED, 23, contact2, 2, account1);
        Opportunity opportunity3 = new Opportunity(Product.HYBRID, 77, contact3, 2, account2);

        opportunity2.setStatus(Status.CLOSED_LOST);
        opportunity3.setStatus(Status.CLOSED_WON);
        opportunityRepository.saveAll(List.of(opportunity1, opportunity2, opportunity3));
        contactRepository.saveAll(List.of(contact1,contact2, contact3));

        account1.setContactList(List.of(contact1,contact2));
        account2.setContactList(List.of(contact3));

        opportunity1.setAccount(account1);
        opportunity2.setAccount(account1);
        opportunity3.setAccount(account2);
        contact1.setAccount(account1);
        contact2.setAccount(account1);
        contact3.setAccount(account2);
        contactRepository.saveAll(List.of(contact1,contact2, contact3));

        opportunityRepository.saveAll(List.of(opportunity1,opportunity2,opportunity3));
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void getOpportunity() {
        Integer id = opportunityRepository.findAll().get(0).getId();
        System.out.println(id);
        OpportunityDTO result = opportunityService.getOpportunity(id);
        System.out.println(result.getProduct());
        System.out.println(result.getQuantity());
        assertEquals(40, result.getQuantity());
        assertEquals(Product.BOX.toString(), result.getProduct());
        assertEquals(1, result.getSalesRepId());
    }

    @Test
    void getAllOpportunities() {
        List<OpportunityDTO> result = opportunityService.getAllOpportunities();
        assertEquals(40, result.get(0).getQuantity());
        assertEquals(Product.FLATBED.toString(), result.get(1).getProduct());
        assertEquals(2, result.get(2).getSalesRepId());
    }

    @Test
    void postOpportunity() {
        Account account = accountRepository.findAll().get(0);
        Contact contact = new Contact("Mario", "999999999", "mario@mario.es", "Bros", account);
        contact = contactRepository.save(contact);
        OpportunityDTO opportunityDTO = new OpportunityDTO(null, "BOX", 40, contact.getId(), 1, account.getId(), "OPEN");
        opportunityService.postOpportunity(opportunityDTO);
        
    }

    @Test
    void putOpportunity() {
    }


    @Test
    void findOpportunitiesBySalesRep() {
        List<OpportunityDTO> result = opportunityService.findOpportunitiesBySalesRep(2, status1);
        assertEquals(23, result.get(0).getQuantity());
        assertEquals(Product.HYBRID.toString(), result.get(1).getProduct());
        assertEquals(2, result.get(1).getSalesRepId());
    }

    @Test
    void findOpportunitiesBySalesRepWithStatus() {
        List<OpportunityDTO> result = opportunityService.findOpportunitiesBySalesRep(2, status2);
        assertEquals(0, result.size());
    }
}