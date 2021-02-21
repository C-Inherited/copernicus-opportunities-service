package com.copernicus.opportunity.model;

import com.copernicus.opportunity.enums.Industry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "account", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)  // we need this to have several "eager"
    @JsonIgnore
    private List<Contact> contactList = new ArrayList<>();

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "account", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Opportunity> opportunityList = new ArrayList<>();


    public Account() {
    }

    public Account(Industry industry, int employeeCount, String city, String country,
                   Contact contact, Opportunity opportunity) {
        this.employeeCount = employeeCount;
        this.city = city;
        this.country = country;
        this.industry = industry;
        this.contactList.add(contact);
        this.opportunityList.add(opportunity);
    }

    public Account(Industry industry, int employeeCount, String city, String country,
                   List<Contact> contacts, List<Opportunity> opportunities) {
        this.employeeCount = employeeCount;
        this.city = city;
        this.country = country;
        this.industry = industry;
        for (Contact contact : contacts){
            this.contactList.add(contact);
        }
        for (Opportunity opportunity : opportunities){
            this.opportunityList.add(opportunity);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
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

    @Override
    public String toString() {
        return ANSI_CYAN + ANSI_BOLD +
                "Account " + id +
                ANSI_RESET + ANSI_BLUE +
                "\nindustry = " + industry +
                "\nemployeeCount = " + employeeCount +
                ", \ncity = " + city +
                ", \ncountry = " + country +
                ", \nopportunityList:\n" + opportunityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return employeeCount == account.employeeCount && industry == account.industry && Objects.equals(city, account.city) && Objects.equals(country, account.country) && Objects.equals(contactList, account.contactList) && Objects.equals(opportunityList, account.opportunityList);
    }
}
