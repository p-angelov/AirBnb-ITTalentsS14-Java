package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.dto.userDTOs.*;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService extends AbstractService {


    public UserResponseDto register(UserRegisterDto userRegistrationForm) {
        if (!isValidUsername(userRegistrationForm.getUsername())) {
            throw new BadRequestException("Username is not valid");
        }
        if (!isValidPassword(userRegistrationForm.getPassword())) {
            throw new BadRequestException("Password is not valid");
        }
        if (!userRegistrationForm.getPassword().equals(userRegistrationForm.getConfirmPassword())) {
            throw new BadRequestException("Passwords not match");
        }
        if (!isValidPhoneNumber(userRegistrationForm.getPhoneNumber())) {
            throw new BadRequestException("Not valid phone number");
        }
        if (!isValidEmail(userRegistrationForm.getEmail())) {
            throw new BadRequestException("Email is not valid");
        }

        if (!isUsernameFree(userRegistrationForm.getUsername())) {
            throw new BadRequestException("This username already exists");
        }
        if (!isEmailFree(userRegistrationForm.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (!isValidBirthDate(userRegistrationForm.getDateOfBirth())) {
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
        return modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto login(UserLoginDto userLoginDto) {
        if (!isValidUsername(userLoginDto.getUsername()) || !isValidPassword(userLoginDto.getPassword())) {
            throw new BadRequestException("Wrong Credentials!");
        }
        User u = userRepository.findUserByUsernameAndPassword(userLoginDto.getUsername(), userLoginDto.getPassword()).orElseThrow(() -> new BadRequestException("Wrong credentials!"));
        UserResponseDto dto = modelMapper.map(u, UserResponseDto.class);
        return dto;

    }

    public void changeHostStatus(UserHostStatusDto dto, long id) {
        User u = getUserById(id);
        u.setHost(dto.isHostStatus());
        userRepository.save(u);
    }


    private boolean isValidUsername(String username) {
        String USERNAME_PATTERN =
                "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
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

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isEmailFree(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    private boolean isUsernameFree(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    private boolean isValidBirthDate(LocalDate birthDate) {
        if (birthDate.getYear() > 2004) return false;
        return true;
    }

    public void changePassword(UserChangePasswordDto dto, long id) {
        if (!isValidPassword(dto.getNewPassword())) {
            throw new BadRequestException("Password is too Weak!");
        }
        User u = getUserById(id);
        if (dto.getCurrentPassword().equals(u.getPassword())) {
            if (dto.getNewPassword().equals(dto.getConfirmPassword())) {
                u.setPassword(dto.getNewPassword());
                userRepository.save(u);
            } else {
                throw new BadRequestException("Passwords doesnt match!");
            }
        } else {
            throw new BadRequestException("Wrong password!");

        }

    }

    public void edit(UserRegisterDto dto, Long id) {
        User u = getUserById(id);
        if (dto.getPassword().equals(u.getPassword()) && dto.getPassword().equals(dto.getConfirmPassword())) {
            dto = setNullFieldsToDefault(dto, id);
            if (!isValidUsername(dto.getUsername()) ||
                    (!isUsernameFree(dto.getUsername()) && !(dto.getUsername().equals(u.getUsername())))
            ) {
                throw new BadRequestException("New username is not valid or already exists!");
            }
            if (!isValidEmail(dto.getEmail()) ||
                    (!isEmailFree(dto.getEmail())&& !(dto.getEmail().equals(u.getEmail())))) {
                throw new BadRequestException("New email is not valid or already exists!");
            }
            if (!isValidBirthDate(dto.getDateOfBirth())) {
                throw new BadRequestException("New date of birth is not valid!");
            }
            if (!isValidPhoneNumber(dto.getPhoneNumber())) {
                throw new BadRequestException("New phone number is not valid ");
            }

            u.setUsername(dto.getUsername());
            u.setEmail(dto.getEmail());
            u.setPhoneNumber(dto.getPhoneNumber());
            u.setDateOfBirth(dto.getDateOfBirth());

            userRepository.save(u);

        } else {
            throw new BadRequestException("Wrong credentials!");
        }
    }

    private UserRegisterDto setNullFieldsToDefault(UserRegisterDto dto, long id) {
        if (dto.getUsername() == null) {
            dto.setUsername(getUserById(id).getUsername());
        }
        if(dto.getEmail() == null){
          dto.setEmail(getUserById(id).getEmail());
        }
        if(dto.getDateOfBirth() == null){
            dto.setDateOfBirth(getUserById(id).getDateOfBirth());
        }
        if(dto.getPhoneNumber() == null){
            dto.setPhoneNumber(getUserById(id).getPhoneNumber());
        }
        return dto;
    }
}
