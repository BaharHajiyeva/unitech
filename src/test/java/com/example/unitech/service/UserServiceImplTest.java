package com.example.unitech.service;

import com.example.unitech.DTO.UserDTO;
import com.example.unitech.entity.User;
import com.example.unitech.payload.LoginPayload;
import com.example.unitech.payload.RegisterPayload;
import com.example.unitech.repository.UserRepository;
import com.example.unitech.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;


import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {


    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepo;

    @Mock
    ModelMapper modelMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    Authentication authentication;

    @Mock
    JwtUtil jwtUtil;

    @Test
    public void testRegister(){

        RegisterPayload payload = new RegisterPayload("dummy","dummy","dummy","dummy");
        User user = new User(1L,"dummy","dummy","dummy","dummy",new ArrayList<>());
        UserDTO userDTO = new UserDTO();
        userDTO.setName("dummy");
        userDTO.setSurname("dummy");

        Mockito.when(userRepo.countByPin("dummy")).thenReturn(0L);
        Mockito.when(modelMapper.map(payload, User.class)).thenReturn(user);
        Mockito.when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        Mockito.when(passwordEncoder.encode("dummy")).thenReturn("dummyToken");

        UserDTO result = userService.register(payload);
        assertEquals(result.getName(),payload.getName());
    }

    @Test
    public void testGetToken(){

        LoginPayload payload = new LoginPayload();
        payload.setPin("dummy");
        payload.setPassword("dummy");

        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("dummy","dummy")))
                .thenReturn(authentication);
        OngoingStubbing<String> stringOngoingStubbing = Mockito.when(jwtUtil.generateToken("dummy"))
                .thenReturn("dummyToken");

        String result = userService.login(payload);

        assertNotSame(result,stringOngoingStubbing);


    }

}
