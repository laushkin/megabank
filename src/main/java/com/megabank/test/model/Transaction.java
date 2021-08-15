package com.megabank.test.model;

import javax.persistence.*;

@Entity
@Table
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String place;
    @Column
    private Float amount;
    @Column
    private Currency currency;
    @Column
    private String card;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "inn")
    private Client client;

    public Transaction() { }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getPlace() { return place; }

    public void setPlace(String place) { this.place = place; }

    public Float getAmount() { return amount; }

    public void setAmount(Float amount) { this.amount = amount; }

    public Currency getCurrency() { return currency; }

    public void setCurrency(Currency currency) { this.currency = currency; }

    public String getCard() { return card; }

    public void setCard(String card) { this.card = card; }

    public Client getClient() { return client; }

    public void setClient(Client client) { this.client = client; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", place='" + place + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                ", card='" + card + '\'' +
                ", client=" + client +
                '}';
    }
}
