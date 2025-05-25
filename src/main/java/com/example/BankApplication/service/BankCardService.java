package com.example.BankApplication.service;


import com.example.BankApplication.exception.BankCardException;
import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.BankCard;
import com.example.BankApplication.model.Transaction;
import com.example.BankApplication.model.User;
import com.example.BankApplication.repository.BankCardRepository;
import com.example.BankApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

@Service
public class BankCardService {

    private EmailService emailService;
    private BankCardRepository bankCardRepository;
    private AccountService accountService;
    private TransactionRepository transactionRepository;
    private UserService userService;

    @Autowired
    public BankCardService(BankCardRepository bankCardRepository,
                           UserService userService,
                           EmailService emailService,
                           TransactionRepository transactionRepository,
                           AccountService accountService) {
        this.userService = userService;
        this.emailService = emailService;
        this.accountService = accountService;
        this.bankCardRepository = bankCardRepository;
        this.transactionRepository = transactionRepository;
    }


    public void cardBankCheck(String cvv,
                              String cardNumber,
                              String expirationDate,
                              double shuma) {
        System.out.println("HYRI NE cardBankCheck");

        Optional<BankCard> optionalCard = bankCardRepository.findByCvvAndCardNumberAndExpirationDate(
                cvv,
                cardNumber,
                expirationDate);

        if (optionalCard.isEmpty()) {
            throw new BankCardException("Karta nuk u gjet.");
        }

        BankCard bankCard1 = optionalCard.get();
        Account account = bankCard1.getAccountNumber();

        System.out.println("account   " + account);
        if (shuma > account.getBalance()) {
            throw new RuntimeException("Fonde te pa mjaftueshme");

        }
        double newBalance = account.getBalance() - shuma;
        System.out.println("Shuma" + shuma);
        account.setBalance(newBalance);
        accountService.updateBalance(account.getId(), newBalance);

        Transaction transaction = new Transaction();
        transaction.setType("Blerje online");
        transaction.setAccount(account);
        transaction.setAmount(shuma);
        transaction.setDate(new Date());
        User uSer = account.getUser();

        emailService.sendEmail(uSer.getEmail(),
                "banka", "kreditim blerje online " + shuma
                        + " data:" + new Date());


        transactionRepository.save(transaction);
    }


    public BankCard addBankCard(BankCard bankCard, String numberAccount) {
        Account account = accountService.getAccount(numberAccount);

        bankCard.setAccountNumber(account);
        return bankCardRepository.save(bankCard);
    }


}
