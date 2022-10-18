package com.ittalents.airbnb.model.dto.propertyDTOs;

import com.ittalents.airbnb.model.dto.userDTOs.UserWithoutPropertiesDto;
import lombok.Data;

import java.util.List;

@Data
public class GeneralPropertyResponseDto {

    private int id;
    private String name;
    private double pricePerNight;
    private List<String> propertyPhotos;
    private String city;

    private UserWithoutPropertiesDto host;
}
