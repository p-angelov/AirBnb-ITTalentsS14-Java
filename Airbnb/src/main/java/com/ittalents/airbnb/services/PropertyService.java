package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.dto.addressDto.FullAddressDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.GeneralPropertyResponseDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyCreationDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserWithoutPropertiesDto;
import com.ittalents.airbnb.model.entity.Address;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.repository.PropertyRepository;
import com.ittalents.airbnb.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyService extends AbstractService{


    public PropertyCreationDto add(PropertyCreationDto dto,long id){ //todo bitwise operations with extras
        //todo photo
        if(!getUserById(id).isHost()){
            throw new BadRequestException("User is not host");
        }
        Property p =modelMapper.map(dto,Property.class);
        System.out.println(p.toString());
        p.setHost(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!")));
        Address a = new Address();
        a.setCountry(dto.getCountry());
        a.setCity(dto.getCity());
        a.setStreet(dto.getStreet());
        a.setNumber(dto.getNumber());
        p.setAddress(a);
        p.setHost(userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found!")));
        a.setProperty(p);
        propertyRepository.save(p);
        return dto;
    }

    public GeneralPropertyResponseDto getPropertyById(long id) {
       // propertyRepository.findById(id);
        Property p = propertyRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        GeneralPropertyResponseDto dto = modelMapper.map(p, GeneralPropertyResponseDto.class);
        return dto;
    }

    public List<GeneralPropertyResponseDto> findAll() {
        List<GeneralPropertyResponseDto> res = new ArrayList<>();
        for(Property p : propertyRepository.findAll()){
            GeneralPropertyResponseDto dto = modelMapper.map(p, GeneralPropertyResponseDto.class);
            res.add(dto);
        }
        return res;
    }
}
