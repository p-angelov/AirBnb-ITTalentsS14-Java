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
import com.ittalents.airbnb.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("properties/add")

    public PropertyCreationDto add(@RequestBody PropertyCreationDto dto, HttpServletRequest request){
         SessionManager.validateLogin(request);
        return propertyService.add(dto, (Long)request.getSession().getAttribute(SessionManager.USER_ID));
    }
    @GetMapping("/properties/{id}")
    //todo Във properties добавянето трябва да стане без user id, а чрез сесията, ако си логнат и ако си хост
    public GeneralPropertyResponseDto getById(@PathVariable long id){
        return propertyService.getPropertyById(id);
    }

    @GetMapping("/properties")
    public List<GeneralPropertyResponseDto> getAll(){
        return propertyService.findAll();
    }


}
