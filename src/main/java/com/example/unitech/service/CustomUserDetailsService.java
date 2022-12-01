package com.example.unitech.service;

import com.example.unitech.entity.User;
import com.example.unitech.exception.NotFoundException;
import com.example.unitech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByPin(pin);

        if(user.isPresent()){
            return new org.springframework.security.core.userdetails.User(user.get().getPin(),user.get().getPassword(),new ArrayList<>());
        } else {
            throw new NotFoundException("İstifadəçi tapılmadı");
        }

    }

}
