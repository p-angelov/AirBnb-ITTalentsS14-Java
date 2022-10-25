package com.ittalents.airbnb.model.dto.propertyDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ittalents.airbnb.model.dto.PhotoDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserWithoutPropertiesDto;
import com.ittalents.airbnb.model.entity.Photo;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class PropertyResponseDto {

    private long id;
    private String name;
    private String description;

    private int maxGuests;
    private int size;
    private double pricePerNight;
    private List<Photo> propertyPhotos;
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
