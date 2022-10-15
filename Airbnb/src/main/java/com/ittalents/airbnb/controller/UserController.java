package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.exceptions.BadRequestException;
import com.ittalents.airbnb.model.dto.UserRegisterDto;
import com.ittalents.airbnb.model.dto.UserResponseDto;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.repository.UserRepository;
import com.ittalents.airbnb.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends MasterController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterDto u){
        UserResponseDto userResponseDto = userService.register(u);
        return ResponseEntity.ok(userResponseDto);
    }



}
