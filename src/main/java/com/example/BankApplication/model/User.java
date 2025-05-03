package com.example.BankApplication.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    Roles roles;

    @OneToMany(mappedBy = "user")
    List<Account> accountList = new ArrayList<>();

    private String email;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }


    @PrePersist
    public void assignDefaultRole() {
        if (roles == null) {
            roles = Roles.user;
        }
    }


    public Roles getRoles() {
        return roles;
    }

    public List<Account> getAccountList() {
        return accountList;
    }
}
