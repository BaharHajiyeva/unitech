package com.example.unitech.service;

import com.example.unitech.DTO.UserDTO;
import com.example.unitech.payload.LoginPayload;
import com.example.unitech.payload.RegisterPayload;

public interface UserService {

    UserDTO register(RegisterPayload payload);

    String login(LoginPayload payload);

}
