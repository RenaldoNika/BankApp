package com.example.BankApplication.service;

import com.example.BankApplication.exception.AccountException;
import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.Transaction;
import com.example.BankApplication.repository.AccountRepository;
import com.example.BankApplication.repository.TransactionRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class BankService {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public BankService(AccountRepository accountRepository,
                       TransactionRepository transactionRepository,
                       EmailService emailService){
        this.accountRepository=accountRepository;
        this.emailService=emailService;
        this.transactionRepository=transactionRepository;
    }
    public Account createAccount(String accountNumber) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(0.0);
        return accountRepository.save(account);
    }
    public Account getAccountBynumber(String number){

        for (Account account:accountRepository.findAll()){
            if (account.getAccountNumber().equals(number))
                return account;
        }
        throw  new AccountException("nuk  egzsiton");

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

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("roniii2017@icloud.com");
            helper.setTo("kristii.9@icloud.com");
            helper.setSubject("deposite");
            helper.setText("llogaria juaj u depositua me "+amount);

            // Dërgo email-in
            mailSender.send(message);

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) {
            throw new AccountException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType("WITHDRAWAL");
        transaction.setDate(new Date());

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("roniii2017@icloud.com");
            helper.setTo("redonanika@icloud.com");
            helper.setSubject("WITHDRAWAL");
            helper.setText("llogaria juaj u depitu me "+amount);

            // Dërgo email-in
            mailSender.send(message);

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }


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

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        Transaction withdrawalTransaction = new Transaction();
        withdrawalTransaction.setAccount(fromAccount);
        withdrawalTransaction.setAmount(amount);
        withdrawalTransaction.setType("Transfer");
        withdrawalTransaction.setDate(new Date());
        transactionRepository.save(withdrawalTransaction);

        toAccount.setBalance(toAccount.getBalance() + amount);
        Transaction depositTransaction = new Transaction();
        depositTransaction.setAccount(toAccount);
        depositTransaction.setAmount(amount);
        depositTransaction.setType("TrasnferFrom:"+fromAccount.getAccountNumber());
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
        List<Transaction> transactionList = account.getTransactionList();
        return transactionList;
    }

}

