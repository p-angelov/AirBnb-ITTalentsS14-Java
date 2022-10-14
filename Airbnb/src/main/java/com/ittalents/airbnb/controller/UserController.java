package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.dto.UserResponseDto;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/users")
    public UserResponseDto register(@RequestBody User u){

        // System.out.println(u.toString());
        userRepository.save(u);
        UserResponseDto userResponseDto = modelMapper.map(u, UserResponseDto.class);
        return userResponseDto;
    }
    private boolean isValid(User u){
        //todo write validations
        if(!u.hasValidEmail()) {

            return false;
        }
        if(!u.hasStrongPassword()) {
            return false;
        }
        if(!u.isEmailTaken()){
            return false;
        }
        if(!u.isUsernameTaken()){
            return false;
        }
        if(!u.isUserNameValid()){
            return false;
        }
        if(!u.isPhoneNumberValid()){
            return false;
        }

        if(!u.isPhoneNumberTaken()){
            return false;
        }

        if(!u.isBirthDateValid()){
            return false;
        }
       return true;
    }




}
