package com.ittalents.airbnb.model.dto.propertyDTOs;

import lombok.Data;

import javax.persistence.Column;

@Data
public class PropertyCharacteristicsDto {
    private String country;
    private String city;
    private int rooms;
    private int bathrooms;
    private int beds;
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
}
