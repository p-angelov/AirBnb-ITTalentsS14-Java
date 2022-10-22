package com.ittalents.airbnb.model.dto.userDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserEditProfileDto {
    private String username;
    private String email;
    private  String phoneNumber;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate dateOfBirth;
}
