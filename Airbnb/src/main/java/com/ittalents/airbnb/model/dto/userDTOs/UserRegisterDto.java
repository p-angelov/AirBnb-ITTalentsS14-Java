package com.ittalents.airbnb.model.dto.userDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;

import java.time.LocalDate;

@Data
public class UserRegisterDto {
    private String username;
    private String password;
    private String confirmPassword;
    private  String phoneNumber;
    private String email;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate dateOfBirth;
    private String profilePictureUrl;
    private boolean isHost;
}
