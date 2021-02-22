package com.copernicus.opportunity.model;

import com.copernicus.opportunity.enums.*;

import javax.persistence.*;

import static com.copernicus.opportunity.utils.Colors.*;

@Entity
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Product product;
    private Integer quantity;

    @OneToOne
    @JoinColumn(name="contact_id")
    private Contact decisionMaker;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer salesRepId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Opportunity() {
    }

    public Opportunity(Product product, Integer quantity,
                       Contact decisionMaker, Integer salesRepId, Account account) {
        setProduct(product);
        setQuantity(quantity);
        setDecisionMaker(decisionMaker);
        setSalesRepId(salesRepId);
        setAccount(account);
        status = Status.OPEN;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Contact getDecisionMaker() {
        return decisionMaker;
    }

    public void setDecisionMaker(Contact decisionMaker) {
        this.decisionMaker = decisionMaker;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getSalesRepId() {
        return salesRepId;
    }

    public void setSalesRepId(Integer salesRepId) {
        this.salesRepId = salesRepId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return ANSI_CYAN + ANSI_BOLD +
                "Opportunity " + id +
                ANSI_RESET + ANSI_BLUE +
                "\nproduct = " + product +
                "\namount = " + quantity +
                ", \nstatus = " + status +
                ", \nsales rep = " + salesRepId +
                ", \n" + decisionMaker + "\n";
    }

}
