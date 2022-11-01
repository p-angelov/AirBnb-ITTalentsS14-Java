package com.ittalents.airbnb.model.dto.propertyDTOs;

import com.ittalents.airbnb.model.dto.PhotoDto;
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
    private List<PhotoDto> propertyPhotos;
    private ShortAddressDto address;

    private boolean hasWifi;
    private boolean hasBalcony;
    private boolean hasAirConditioning;
    private boolean hasWashingMachine;
    private boolean hasDishWasher;
    private boolean hasBabyCrib;
    private boolean hasYard;
    private boolean hasParking;
    private boolean hasKitchen;
    private boolean hasTV;
    private boolean hasChildrenPlayground;

    private UserWithoutPropertiesDto host;
}
