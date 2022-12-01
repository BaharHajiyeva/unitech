package com.example.unitech.repository;

import com.example.unitech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    long countByPin(String pin);

    Optional<User> findByPin(String pin);

}
