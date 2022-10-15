package com.ittalents.airbnb.services;

import com.ittalents.airbnb.exceptions.BadRequestException;
import com.ittalents.airbnb.exceptions.NotFoundException;
import com.ittalents.airbnb.model.dto.UserRegisterDto;
import com.ittalents.airbnb.model.dto.UserResponseDto;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.repository.UserRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    public UserResponseDto register(UserRegisterDto userRegistrationForm){
        if(!isValidUsername(userRegistrationForm.getUsername())){
            throw new BadRequestException("Username is not valid");
        }
        if(!isValidPassword(userRegistrationForm.getPassword())){
            throw new BadRequestException("Password is not valid");
        }
        if(!userRegistrationForm.getPassword().equals(userRegistrationForm.getConfirmPassword())){
            throw new BadRequestException("Passwords not match");
        }
        if(!isValidPhoneNumber(userRegistrationForm.getPhoneNumber())){
            throw new BadRequestException("Not valid phone number");
        }
        if(!isValidEmail(userRegistrationForm.getEmail())){
            throw new BadRequestException("Email is not valid");
        }

        if(!isUsernameFree(userRegistrationForm.getUsername())){
            throw new BadRequestException("This username already exists");
        }
        if(!isEmailFree(userRegistrationForm.getEmail())){
            throw new BadRequestException("Email already exists");
        }
        if(!isValidBirthDate(userRegistrationForm.getDateOfBirth())){
            throw new BadRequestException("User is under 18");
        }
        //todo validate for valid photo url
        //todo validate Host?
        User user = new User();
        user.setUsername(userRegistrationForm.getUsername());
        user.setPassword(userRegistrationForm.getPassword());
        //todo Encrypt password
        user.setEmail(userRegistrationForm.getEmail());
        user.setPhoneNumber(userRegistrationForm.getPhoneNumber());
        user.setProfilePictureUrl(userRegistrationForm.getProfilePictureUrl());
        user.setDateOfBirth(userRegistrationForm.getDateOfBirth());
        userRepository.save(user);
        return modelMapper.map(user,UserResponseDto.class);
    }
    public User getById(long id){
        if(userRepository.findById(id).isPresent()){
            return userRepository.findById(id).get();
        }
        else{
            throw new NotFoundException("There is no user with id " + id  );
        }
    }
    private boolean isValidUsername(String username){
        String USERNAME_PATTERN =
                "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
   private boolean isValidPassword(String password){
       String regex = "^(?=.*[0-9])"
               + "(?=.*[a-z])(?=.*[A-Z])"
               + "(?=.*[@#$%^&+=])"
               + "(?=\\S+$).{8,20}$";

       Pattern p = Pattern.compile(regex);
       if (password == null) {
           return false;
       }
       Matcher m = p.matcher(password);
       return m.matches();
   }

    private boolean isValidPhoneNumber(String phoneNumber){
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    private boolean isValidEmail(String email){
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isEmailFree(String email){
        return userRepository.findByEmail(email).isEmpty();
    }
    private boolean isUsernameFree(String username){
        return userRepository.findByUsername(username).isEmpty();
    }
    private boolean isValidBirthDate(LocalDate birthDate){
        if(birthDate.getYear()>2004) return false;
        return true;
    }
}
