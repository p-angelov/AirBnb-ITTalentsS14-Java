package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.dto.propertyDTOs.GeneralPropertyResponseDto;
import com.ittalents.airbnb.model.dto.userDTOs.*;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.repository.UserRepository;
import com.ittalents.airbnb.util.ValidationUtil;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService {


    public UserResponseDto register(UserRegisterDto userRegistrationForm) {
        ValidationUtil.validateRegistration(userRegistrationForm);

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
        if (!ValidationUtil.isValidUsername(userLoginDto.getUsername()) || !ValidationUtil.isValidPassword(userLoginDto.getPassword())) {
            throw new BadRequestException("Wrong Credentials!");
        }
        User u = userRepository.findUserByUsernameAndPassword(userLoginDto.getUsername(), userLoginDto.getPassword()).orElseThrow(() -> new BadRequestException("Wrong credentials!"));
        UserResponseDto dto = modelMapper.map(u, UserResponseDto.class);
        return dto;
    }

    public void uploadProfilePicture(MultipartFile f, long id){
        validatePhoto(f);
        User u = getUserById(id);
        String ext = FilenameUtils.getExtension(f.getOriginalFilename());
        String folder = "photos"+ File.separator + "UserPhotos";
        String fileName =   System.nanoTime() + new Random().nextInt(999999) + "." + ext;
        File newFile = new File(folder+File.separator+fileName);
        if(!newFile.exists()){
            try {
               // newFile.createNewFile();
                Files.copy(f.getInputStream(),newFile.toPath());
                if(u.getProfilePictureUrl()!=null){
                    File old = new File(u.getProfilePictureUrl());
                    old.delete();
                }
                u.setProfilePictureUrl(fileName);
                userRepository.save(u);
            } catch (IOException e) {
                e.printStackTrace();
                throw new BadRequestException("Error",e);
            }
        }
    }

    public void changeHostStatus(UserHostStatusDto dto, long id) {
        User u = getUserById(id);
        u.setHost(dto.isHostStatus());
        userRepository.save(u);
    }




    public void changePassword(UserChangePasswordDto dto, long id) {
        if (!ValidationUtil.isValidPassword(dto.getNewPassword())) {
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

    public void edit(UserEditProfileDto dto, Long id) {
        User u = getUserById(id);
        ValidationUtil.validateEdit(dto,u);
            u.setUsername(dto.getUsername());
            u.setEmail(dto.getEmail());
            u.setPhoneNumber(dto.getPhoneNumber());
            u.setDateOfBirth(dto.getDateOfBirth());
            userRepository.save(u);
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
