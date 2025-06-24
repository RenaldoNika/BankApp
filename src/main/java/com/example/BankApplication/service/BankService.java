package com.example.BankApplication.service;

import com.example.BankApplication.exception.AccountException;
import com.example.BankApplication.exception.BankCardException;
import com.example.BankApplication.model.*;
import com.example.BankApplication.repository.AccountRepository;
import com.example.BankApplication.repository.BankCardRepository;
import com.example.BankApplication.repository.TransactionRepository;
import com.example.BankApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class BankService {

    private BankCardService bankCardService;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private EmailService emailService;
    private UserRepository userRepository;
    private DtoUserContextSpringHolder dtoUserContextSpringHolder;
    private BankCardRepository bankCardRepository;


    @Autowired
    public BankService(AccountRepository accountRepository,
                       BankCardRepository bankCardRepository,
                       BankCardService bankCardService,
                       DtoUserContextSpringHolder dtoUserContextSpringHolder,
                       UserRepository userRepository,
                       TransactionRepository transactionRepository,
                       EmailService emailService) {
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.dtoUserContextSpringHolder = dtoUserContextSpringHolder;
        this.bankCardRepository = bankCardRepository;
    }

    public Account createAccount(String accountNumber) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(0.0);
        return accountRepository.save(account);
    }

    public Account getAccountBynumber(String number) {

        for (Account account : accountRepository.findAll()) {
            if (account.getAccountNumber().equals(number))
                return account;
        }
        throw new AccountException("nuk  egzsiton");

    }

    public void deposit(String accountNumber, double amount) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).get();

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType("WITHDRAW");

        transaction.setDate(new Date());

        emailService.sendEmail(email,
                "banka", "kreditim " + amount
                        + " data:" + new Date());

        transactionRepository.save(transaction);
        accountRepository.save(account);
    }


    public void withdraw(String accountNumber, double amount) {

        User userFrom = dtoUserContextSpringHolder.getCurrentUser();

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

        emailService.sendEmail(userFrom.getEmail(),
                "banka", "kreditim " + amount
                        + " data:" + new Date());


        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    public void trasnferToShop(String shopAccountnumber,
                               String cvv,
                               String cardNumber,
                               String expirationDate,
                               double amount) {


        BankCard bankCard = bankCardRepository.findByCvvAndCardNumberAndExpirationDate(
                cvv,
                cardNumber,
                expirationDate).orElseThrow(() -> new BankCardException("gabim ne karte"));

        String accountNumberBuy = bankCard.getAccountNumber().getAccountNumber();


        Account accountUser = accountRepository.findByAccountNumber(accountNumberBuy).orElseThrow(
                () -> new AccountException("nuk egziston"));


        Account shopAccount = accountRepository.findByAccountNumber(shopAccountnumber)
                .orElseThrow(() -> new AccountException("To Account not found"));

        accountUser.setBalance(accountUser.getBalance() - amount);

        shopAccount.setBalance(shopAccount.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDate(new Date());
        transaction.setType("online");
        transaction.setAccount(accountUser);
        transactionRepository.save(transaction);

        Transaction transactionShop = new Transaction();
        transactionShop.setAmount(amount);
        transactionShop.setDate(new Date());
        transactionShop.setType("online");
        transactionShop.setAccount(shopAccount);
        transactionRepository.save(transactionShop);


    }

    public void transfer(String toAccountNumber, double amount) {

        User userFrom = dtoUserContextSpringHolder.getCurrentUser();
        Account accountFrom = userFrom.getAccountList().get(0);

        double shumafrom = accountFrom.getBalance();

        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("To Account not found"));
        if (shumafrom < amount) {
            throw new AccountException("Insufficient funds");
        }

        accountFrom.setBalance(accountFrom.getBalance() - amount);
        Transaction withdrawalTransaction = new Transaction();
        withdrawalTransaction.setAccount(accountFrom);
        withdrawalTransaction.setAmount(amount);
        withdrawalTransaction.setType("Transfer " + " TO: " + toAccountNumber);
        withdrawalTransaction.setDate(new Date());
        transactionRepository.save(withdrawalTransaction);

        toAccount.setBalance(toAccount.getBalance() + amount);
        Transaction depositTransaction = new Transaction();
        depositTransaction.setAccount(toAccount);
        depositTransaction.setAmount(amount);
        depositTransaction.setType("TransferFrom:" + accountFrom.getAccountNumber());
        depositTransaction.setDate(new Date());
        transactionRepository.save(depositTransaction);

        accountRepository.save(accountFrom);
        accountRepository.save(toAccount);

        User user = userRepository.findByAccountNumber(toAccountNumber);
        emailService.sendEmail(user.getEmail(),
                "Banka", "Kreditim prej "
                        + amount + " në llogarinë tuaj. Data: "
                        + "nga llogaria " + userFrom.getEmail()
                        + new Date());
    }


    public double getBalance() {
        User userLogin = dtoUserContextSpringHolder.getCurrentUser();
        Account account = accountRepository.findByAccountNumber(userLogin.getAccountList().get(0).getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getBalance();
    }

    public double getBalanceForShopin(String numberAccount) {
        Account accounts = accountRepository.findByAccountNumber(numberAccount).get();

        return accounts.getBalance();

    }

    public List<Transaction> getTransactions() {
        User userLogin = dtoUserContextSpringHolder.getCurrentUser();
        Account account = userLogin.getAccountList().get(0);
        Account fullAccount = accountRepository.findByAccountNumber(account.getAccountNumber()).get();
        if (fullAccount == null) {
            throw new RuntimeException("Account not found");
        }
        return fullAccount.getTransactionList();
    }

}

