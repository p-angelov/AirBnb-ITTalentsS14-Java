package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.dto.userDTOs.*;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.exceptions.OkException;
import com.ittalents.airbnb.model.repositories.UserRepository;
import com.ittalents.airbnb.services.UserService;
import com.ittalents.airbnb.util.SessionManager;
import org.apache.http.client.HttpResponseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController extends MasterController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterDto u,HttpServletRequest request){
        if(request.getSession().getAttribute(SessionManager.LOGGED)==null){
            UserResponseDto userResponseDto = userService.register(u);
            return ResponseEntity.ok(userResponseDto);
        }
        throw new BadRequestException("User is already logged");

    }
    @GetMapping("/users/{id}")
    public ResponseEntity<UserInfoDto> getById(@PathVariable("id") long id) {
        User user = userService.getUserById(id);
        UserInfoDto dto = modelMapper.map(user, UserInfoDto.class);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request){
        UserResponseDto user = userService.login(userLoginDto);
        request.getSession().setAttribute(SessionManager.LOGGED,true);
        request.getSession().setAttribute(SessionManager.LOGGED_FROM,request.getRemoteAddr());
        request.getSession().setAttribute(SessionManager.USER_ID,user.getId());
        return ResponseEntity.ok(user);
    }
    @PostMapping("/users/logout")
    public void logout(HttpServletRequest request){
        SessionManager.validateLogin(request);
        request.getSession().invalidate();
        throw new OkException("You have logged out successfully!");
    }
    @PutMapping("/users")
    public UserResponseDto edit(@RequestBody UserEditProfileDto dto , HttpServletRequest request){
        SessionManager.validateLogin(request);
         return userService.edit(dto,(Long)request.getSession().getAttribute(SessionManager.USER_ID));

    }
    @DeleteMapping("/users/delete") //todo fix delete request
    public UserResponseDto deleteUser(HttpServletRequest request) {
        SessionManager.validateLogin(request);
        long userId = (Long) request.getSession().getAttribute(SessionManager.USER_ID);
        request.getSession().invalidate();

       return userService.deleteProfile(userId);
    }
    @PutMapping("/users/password")
    public void changePassword(@RequestBody UserChangePasswordDto dto,HttpServletRequest request){
        SessionManager.validateLogin(request);
        userService.changePassword(dto,(Long)request.getSession().getAttribute(SessionManager.USER_ID));
    }
    @PutMapping("/users/status")
    public UserHostStatusDto changeHostStatus( @RequestBody UserHostStatusDto u,HttpServletRequest request) {
        SessionManager.validateLogin(request);
        userService.changeHostStatus(u,(Long)request.getSession().getAttribute(SessionManager.USER_ID));
        return u;
    }
    @PostMapping("/users/profilePicture")
    public void uploadProfilePicture(@RequestParam(value = "file")MultipartFile f,HttpServletRequest request){
        SessionManager.validateLogin(request);
        long id = (Long) request.getSession().getAttribute(SessionManager.USER_ID);
        userService.uploadProfilePicture(f,id);
    }

}
