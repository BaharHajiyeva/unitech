package com.example.unitech.service;

import com.example.unitech.DTO.UserDTO;
import com.example.unitech.entity.User;
import com.example.unitech.exception.BadRequestException;
import com.example.unitech.exception.NotFoundException;
import com.example.unitech.payload.LoginPayload;
import com.example.unitech.payload.RegisterPayload;
import com.example.unitech.repository.UserRepository;
import com.example.unitech.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    @Override
    public UserDTO register(RegisterPayload payload) {

        if (userRepo.countByPin(payload.getPin()) == 0) {

            User user = modelMapper.map(payload, User.class);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);

            return modelMapper.map(user, UserDTO.class);

        } else {
            throw new BadRequestException("This pin already taken");
        }
    }

    @Override
    public String getToken(LoginPayload payload) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(payload.getPin(), payload.getPassword())
            );
        } catch (Exception ex) {
            throw new BadRequestException("Invalid pin or password");
        }
        return jwtUtil.generateToken(payload.getPin());

    }

}
