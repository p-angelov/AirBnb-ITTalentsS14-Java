package com.ittalents.airbnb.model.dto.addressDto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class FullAddressDto {

    private long id;
    private String street;
    private int number;
    private String city;
    private String country;
}
