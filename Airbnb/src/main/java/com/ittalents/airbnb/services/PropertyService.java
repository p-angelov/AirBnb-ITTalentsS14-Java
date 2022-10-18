package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.dto.propertyDTOs.GeneralPropertyResponseDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyCreationDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserWithoutPropertiesDto;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.repository.PropertyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private ModelMapper modelMapper;


    //todo bitwise operations with extras

    public PropertyCreationDto add(long id){
        Property p = propertyRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        PropertyCreationDto dto = modelMapper.map(p, PropertyCreationDto.class);
        dto.setHost(modelMapper.map(p.getHost(), UserWithoutPropertiesDto.class));
        return dto;
    }

    public GeneralPropertyResponseDto getById(long id) {
        Property p = propertyRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        GeneralPropertyResponseDto dto = modelMapper.map(p, GeneralPropertyResponseDto.class);
        dto.setHost(modelMapper.map(p.getHost(), UserWithoutPropertiesDto.class));
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
