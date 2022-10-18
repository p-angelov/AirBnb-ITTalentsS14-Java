package com.ittalents.airbnb.model.dto.userDTOs;

import lombok.Data;

@Data
public class UserWithoutPropertiesDto {

    private long id;
    private String username;
    private String phoneNumber;
    private String email;
    private String profilePictureUrl;

}
