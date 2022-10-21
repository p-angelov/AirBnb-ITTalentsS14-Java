package com.ittalents.airbnb.model.dto.propertyDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ittalents.airbnb.model.dto.PhotoDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserWithoutPropertiesDto;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class PropertyResponseDto {

    private int id;
    private String name;
    private String description;

    private int maxGuests;
    private int size;
    private double pricePerNight;
    private List<PhotoDto> propertyPhotos;
    private String type;
    private int bathrooms;
    private int beds;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeOfArrival;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime leavingTime;

    private long extras;

    private String street;
    private int number;
    private String city;
    private String country;

    private UserWithoutPropertiesDto host;
}
