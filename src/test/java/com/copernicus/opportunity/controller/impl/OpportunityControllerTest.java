package com.copernicus.opportunity.controller.impl;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class OpportunityControllerTest {

    @Autowired
    OpportunityRepository opportunityRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    IOpportunityService opportunityService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    Optional<String> status1 = Optional.empty();
    Optional<String> status2 = Optional.of("OPEN");


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
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

        contactRepository.saveAll(List.of(contact1,contact2, contact3));
        opportunityRepository.saveAll(List.of(opportunity1, opportunity2, opportunity3));

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
    void getOpportunity() throws Exception {
        Opportunity opportunity = opportunityRepository.findAll().get(0);

        MvcResult result = mockMvc
                .perform(get("/opportunity/"+opportunity.getId())
                        .header("Authorization", "Bearer auth"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        assertTrue(result.getResponse().getContentAsString().contains("BOX"));
    }

    @Test
    void getAllOpportunities() throws Exception {

        MvcResult result = mockMvc
                .perform(get("/opportunity/all")
                        .header("Authorization", "Bearer auth"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        assertTrue(result.getResponse().getContentAsString().contains("BOX"));
        assertTrue(result.getResponse().getContentAsString().contains("FLATBED"));
        assertTrue(result.getResponse().getContentAsString().contains("HYBRID"));
    }

    @Test
    void postOpportunity() throws Exception {
        Account account = accountRepository.findAll().get(0);
        Contact contact = new Contact("Mario", "999999999", "mario@mario.es", "Bros", account);
        contact = contactRepository.save(contact);
        OpportunityDTO opportunityDTO = new OpportunityDTO(null, "BOX", 40, contact.getId(), 1, account.getId(), "OPEN");
        opportunityService.postOpportunity(opportunityDTO);

        String body = objectMapper.writeValueAsString(opportunityDTO);

        MvcResult result = mockMvc
                .perform(post("/opportunity")
                        .header("Authorization", "Bearer auth")
                        .content(body).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("BOX"));
    }

    @Test
    void putOpportunity() throws Exception {
        Opportunity opportunity = opportunityRepository.findAll().get(0);
        OpportunityDTO opportunityDTO = OpportunityDTO.parseFromOpportunity(opportunity);
        opportunityDTO.setQuantity(5654);

        String body = objectMapper.writeValueAsString(opportunityDTO);

        MvcResult result = mockMvc
                .perform(put("/opportunity/"+opportunityDTO.getId())
                        .header("Authorization", "Bearer auth")
                        .content(body).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("5654"));
    }

    @Test
    void findOpportunitiesBySalesRep() {
    }
}