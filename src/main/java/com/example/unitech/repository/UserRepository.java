package com.example.unitech.repository;

import com.example.unitech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    long countByPin(String pin);

    User findByPin(String pin);

}
