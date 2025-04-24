package com.example.BankApplication.configuration;

import com.twilio.Twilio;
import com.twilio.rest.lookups.v1.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
        System.out.println("Ky shërbim u përgatit!");

        String numberToCheck = "+13477429873"; // Vendos numrin që dëshiron të kontrollosh
        checkNumberSupport(numberToCheck);
    }


    public void checkNumberSupport(String phoneNumber) {
        try {
            PhoneNumber number = PhoneNumber.fetcher(new com.twilio.type.PhoneNumber(phoneNumber))
                    .fetch();

            System.out.println("Numri është i vlefshëm dhe mund të përdoret për SMS: " + number.getPhoneNumber());

        } catch (Exception e) {
            System.out.println("Gabim gjatë verifikimit të numrit: " + e.getMessage());
        }

    }
}