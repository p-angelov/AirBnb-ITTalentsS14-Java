package com.ittalents.airbnb.model.dto.propertyDTOs;

import lombok.Data;
@Data
public class ExtrasDto {
    private String extra0;
    private String extra1;
    private String extra2;
    private String extra3;
    private String extra4;
    private String extra5;
    private String extra6;
    private String extra7;
    private String extra8;
    private String extra9;
    private String extra10;
    public ExtrasDto(){
        this.extra0 = "Wifi";
        this.extra1 = "Balcony";
        this.extra2 = "Air Conditioning";
        this.extra3 = "Washing machine";
        this.extra4 = "Dish washer";
        this.extra5 = "Baby crib";
        this.extra6 = "Yard";
        this.extra7 = "Parking";
        this.extra8 = "Kitchen";
        this.extra9 = "TV";
        this.extra10 = "Children playground";
    }
}
