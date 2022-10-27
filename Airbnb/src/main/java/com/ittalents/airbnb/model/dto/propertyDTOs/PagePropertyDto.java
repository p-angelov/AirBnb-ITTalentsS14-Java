package com.ittalents.airbnb.model.dto.propertyDTOs;

import com.ittalents.airbnb.model.dto.PhotoDto;
import com.ittalents.airbnb.model.dto.addressDto.ShortAddressDto;
import com.ittalents.airbnb.model.entity.Address;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
public class PagePropertyDto {
    private int id;
    private String name;
    private double pricePerNight;
    private double rating;
    private List<PhotoDto> propertyPhotos;

    private ShortAddressDto addressF = new ShortAddressDto();

    public void setAddressF(Address address) {
        this.addressF.setCity(address.getCity());
        this.addressF.setCountry(address.getCountry());
        this.addressF.setId(address.getId());
    }
}
