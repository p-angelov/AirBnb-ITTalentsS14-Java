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

    private ShortAddressDto address = new ShortAddressDto();

    public void setAddressDto(Address address) {
        this.address.setCity(address.getCity());
        this.address.setCountry(address.getCountry());
        this.address.setId(address.getId());
    }
}
