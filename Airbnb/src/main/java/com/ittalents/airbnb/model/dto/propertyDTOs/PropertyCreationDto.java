package com.ittalents.airbnb.model.dto.propertyDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ittalents.airbnb.model.dto.addressDto.FullAddressDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserResponseDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserWithoutPropertiesDto;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class PropertyCreationDto {

    private long id;
    private String name;
    private String description;

    private int maxGuests;
    private int size;
    private double pricePerNight;
    private List<String> propertyPhotos;
    private String type;
    private int bathrooms;
    private int beds;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeOfArrival;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime leavingTime;

    //extras
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

    //address
    private String street;
    private int number;
    private String city;
    private String country;

    private UserResponseDto host;

}
