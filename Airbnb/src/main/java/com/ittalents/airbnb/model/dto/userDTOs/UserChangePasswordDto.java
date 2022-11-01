package com.ittalents.airbnb.model.dto.userDTOs;

import lombok.Data;

@Data
public class UserChangePasswordDto {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
