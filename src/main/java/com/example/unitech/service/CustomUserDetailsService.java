package com.example.unitech.service;

import com.example.unitech.entity.User;
import com.example.unitech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        User user = userRepository.findByPin(pin);

        return new org.springframework.security.core.userdetails.User(user.getPin(),user.getPassword(),new ArrayList<>());
    }

}
