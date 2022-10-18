package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.dto.propertyDTOs.GeneralPropertyResponseDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserWithoutPropertiesDto;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyCreationDto;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.repository.PropertyRepository;
import com.ittalents.airbnb.model.repository.UserRepository;
import com.ittalents.airbnb.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PropertyController extends MasterController{

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users/{id}/properties/add")
    public PropertyCreationDto add(@RequestBody PropertyCreationDto dto, @PathVariable("id") long id){
        return propertyService.add(id);
    }
    @GetMapping("/properties/{id}")
    public GeneralPropertyResponseDto getById(@PathVariable long id){
        return propertyService.getById(id);
    }

    @GetMapping("/properties")
    public List<GeneralPropertyResponseDto> getAll(){
        return propertyService.findAll();
    }


}
