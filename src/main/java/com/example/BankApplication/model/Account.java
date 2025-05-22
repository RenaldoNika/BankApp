package com.example.BankApplication.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactionList;


    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "accountNumber")
    private BankCard bankCard;


    private String accountNumber;


    @JsonIgnore
    private double balance;

    private static final AtomicInteger counter = new AtomicInteger(1000);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }


    @PrePersist
    public void generateAccountNumber() {
        String datePart = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int uniquePart = counter.getAndIncrement();
        this.accountNumber = datePart + uniquePart;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }

    public BankCard getBankCard() {
        return bankCard;
    }



}
