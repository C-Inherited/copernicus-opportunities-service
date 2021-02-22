package com.copernicus.opportunity.model;

import com.copernicus.opportunity.dto.AccountDTO;
import com.copernicus.opportunity.enums.Industry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.copernicus.opportunity.utils.Colors.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Industry industry;
    private Integer employeeCount;
    private String city;
    private String country;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Contact> contactList;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "account", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Opportunity> opportunityList;

    // Constructor

    public Account() {
    }

    public Account(Industry industry, int employeeCount, String city, String country) {
        setIndustry(industry);
        setEmployeeCount(employeeCount);
        setCity(city);
        setCountry(country);
        setContactList(new ArrayList<>());
        setOpportunityList(new ArrayList<>());
    }

    public static Account parseFromDTO(AccountDTO accountDTO) {
        Account account = new Account(
                Industry.valueOf(accountDTO.getIndustry()),
                accountDTO.getEmployeeCount(),
                accountDTO.getCity(),
                accountDTO.getCountry()
        );
        account.setId(accountDTO.getId());
        return account;
    }

    // Method to add a contact to a contact list.
    public void addToContactList(Contact contact){
        getContactList().add(contact);
    }

    // Method to add an opportunity to an opportunity list.
    public void addToOpportunityList(Opportunity opportunity){
        getOpportunityList().add(opportunity);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public List<Opportunity> getOpportunityList() {
        return opportunityList;
    }

    public void setOpportunityList(List<Opportunity> opportunityList) {
        this.opportunityList = opportunityList;
    }

// Override of the toString() method to display the Accounts in a more friendly way.

    @Override
    public String toString() {
        return ANSI_CYAN + ANSI_BOLD +
                "Account " + id +
                ANSI_RESET + ANSI_BLUE +
                "\nindustry = " + industry +
                "\nemployeeCount = " + employeeCount +
                ", \ncity = " + city +
                ", \ncountry = " + country +
                ", \ncontact list = " + contactList.stream()
                .map(Contact::toString)
                .collect(Collectors.joining("\n")) +
                ", \nopportunity List:\n" + opportunityList.stream()
                .map(Opportunity::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return employeeCount == account.employeeCount && industry == account.industry && Objects.equals(city, account.city) && Objects.equals(country, account.country) && Objects.equals(contactList, account.contactList) && Objects.equals(opportunityList, account.opportunityList);
    }
}
