package com.ittalents.airbnb.util;

import com.ittalents.airbnb.model.dto.userDTOs.UserEditProfileDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserRegisterDto;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.repositories.UserRepository;
import com.ittalents.airbnb.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class ValidationUtil {

    public static  UserRepository userRepository ;
    @Autowired
    public ValidationUtil(UserRepository userRepository){
        ValidationUtil.userRepository = userRepository;
    }
    public static boolean isValidUsername(String username) {
        String USERNAME_PATTERN =
                "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
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

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isEmailFree(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public static boolean isUsernameFree(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    public static boolean isValidBirthDate(LocalDate birthDate) {
        if (birthDate.getYear() > 2004) return false;
        return true;
    }
    public static void validateEdit(UserEditProfileDto dto,User u){
        if (!ValidationUtil.isValidUsername(dto.getUsername()) ||
                (!ValidationUtil.isUsernameFree(dto.getUsername()) && !(dto.getUsername().equals(u.getUsername())))
        ) {
            throw new BadRequestException("New username is not valid or already exists!");
        }
        if (!ValidationUtil.isValidEmail(dto.getEmail()) ||
                (!ValidationUtil.isEmailFree(dto.getEmail())&& !(dto.getEmail().equals(u.getEmail())))) {
            throw new BadRequestException("New email is not valid or already exists!");
        }
        if (!ValidationUtil.isValidBirthDate(dto.getDateOfBirth())) {
            throw new BadRequestException("New date of birth is not valid!");
        }
        if (!ValidationUtil.isValidPhoneNumber(dto.getPhoneNumber())) {
            throw new BadRequestException("New phone number is not valid ");
        }

    }
    public static void validateRegistration(UserRegisterDto userRegistrationForm) {
        if (!ValidationUtil.isValidUsername(userRegistrationForm.getUsername())) {
            throw new BadRequestException("Username is not valid");
        }
        if (!ValidationUtil.isValidPassword(userRegistrationForm.getPassword())) {
            throw new BadRequestException("Password is not valid");
        }
        if (!userRegistrationForm.getPassword().equals(userRegistrationForm.getConfirmPassword())) {
            throw new BadRequestException("Passwords not match");
        }
        if (!ValidationUtil.isValidPhoneNumber(userRegistrationForm.getPhoneNumber())) {
            throw new BadRequestException("Not valid phone number");
        }
        if (!ValidationUtil.isValidEmail(userRegistrationForm.getEmail())) {
            throw new BadRequestException("Email is not valid");
        }

        if (!ValidationUtil.isUsernameFree(userRegistrationForm.getUsername())) {
            throw new BadRequestException("This username already exists");
        }
        if (!ValidationUtil.isEmailFree(userRegistrationForm.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (!ValidationUtil.isValidBirthDate(userRegistrationForm.getDateOfBirth())) {
            throw new BadRequestException("User is under 18");
        }
    }
}
