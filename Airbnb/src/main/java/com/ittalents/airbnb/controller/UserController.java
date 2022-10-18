package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.dto.userDTOs.UserHostStatusDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserInfoDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserRegisterDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserResponseDto;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.repository.UserRepository;
import com.ittalents.airbnb.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/users/{id}")
    public ResponseEntity<UserInfoDto> getById(@PathVariable("id") long id) {
        User user = userService.getById(id);
        UserInfoDto dto = modelMapper.map(user, UserInfoDto.class);
        return ResponseEntity.ok(dto);
    }
    @PutMapping("/users/{id}/host")
    public ResponseEntity<UserInfoDto> changeHostStatus(@PathVariable("id") long id, @RequestBody UserHostStatusDto u) {
        User user = userService.getById(id);
        System.out.println(u.isHostStatus());
        user.setHost(u.isHostStatus());
        UserInfoDto dto = modelMapper.map(user, UserInfoDto.class);
        return ResponseEntity.ok(dto);
    }

}
