package com.ittalents.airbnb.model.dto.propertyDTOs;

import com.ittalents.airbnb.model.dto.addressDto.ShortAddressDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserWithoutPropertiesDto;
import lombok.Data;

import java.util.List;

@Data
public class GeneralPropertyResponseDto {

    private int id;
    private String name;
    private double pricePerNight;
    private double rating;
    private List<String> propertyPhotos;
    private ShortAddressDto address;

    private UserWithoutPropertiesDto host;
}
