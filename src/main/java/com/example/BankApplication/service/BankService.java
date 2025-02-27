package com.example.BankApplication.service;

import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.Transaction;
import com.example.BankApplication.repository.AccountRepository;
import com.example.BankApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class BankService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public Account createAccount(String accountNumber) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(0.0);
        return accountRepository.save(account);
    }

    public void deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType("DEPOSIT");
        transaction.setDate(new Date());

        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType("WITHDRAWAL");
        transaction.setDate(new Date());

        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("From Account not found"));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("To Account not found"));

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }

        // Tërheqja nga llogaria e parë
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        Transaction withdrawalTransaction = new Transaction();
        withdrawalTransaction.setAccount(fromAccount);
        withdrawalTransaction.setAmount(amount);
        withdrawalTransaction.setType("WITHDRAWAL");
        withdrawalTransaction.setDate(new Date());
        transactionRepository.save(withdrawalTransaction);

        // Depozita në llogarinë e dytë
        toAccount.setBalance(toAccount.getBalance() + amount);
        Transaction depositTransaction = new Transaction();
        depositTransaction.setAccount(toAccount);
        depositTransaction.setAmount(amount);
        depositTransaction.setType("DEPOSIT");
        depositTransaction.setDate(new Date());
        transactionRepository.save(depositTransaction);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    public double getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getBalance();
    }

    public List<Transaction> getTransactions(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByAccount(account);
    }
}

