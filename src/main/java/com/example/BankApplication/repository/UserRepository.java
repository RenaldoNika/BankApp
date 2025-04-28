package com.example.BankApplication.repository;

import com.example.BankApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.accountList a WHERE a.accountNumber = :accountNumber")
    User findByAccountNumber(@Param("accountNumber") String accountNumber);
}
