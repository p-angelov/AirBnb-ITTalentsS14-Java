package com.ittalents.airbnb.model.dto.userDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInfoDto {

    private String username;
    private String phoneNumber;
    private String email;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate dateOfBirth;
    private String profilePictureUrl;
    private boolean isHost;
}
