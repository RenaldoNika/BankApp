package com.example.BankApplication.repository;

import com.example.BankApplication.model.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface BankCardRepository extends JpaRepository<BankCard,Long> {

    Optional<BankCard> findByCvvAndCardNumberAndExpirationDate(String cvv, String cardNumber, String expirationDate);

}
