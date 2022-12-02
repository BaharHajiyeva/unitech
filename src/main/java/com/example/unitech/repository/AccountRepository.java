package com.example.unitech.repository;

import com.example.unitech.entity.Account;
import com.example.unitech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    long countByAccountNumber(String accountNumber);

    List<Account> findAllByUserAndActiveTrue(User user);

    Optional<Account> findByUserAndAccountNumberAndActiveTrue(User user, String accountNumber);

}
