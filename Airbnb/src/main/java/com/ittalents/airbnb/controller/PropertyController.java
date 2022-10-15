package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.exceptions.NotFoundException;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PropertyController extends MasterController{

    @Autowired
    private PropertyRepository propertyRepository;

    @PostMapping("/properties/add")
    public Property add(@RequestBody Property p){
        //todo finish creating a property
        propertyRepository.save(p);
        return p;
    }

    @GetMapping("/properties/{id}")
    public List<Property> getAll(){
        return propertyRepository.findAll();
    }

    public Property getById(@PathVariable long id){
        Optional<Property> p = propertyRepository.findById(id);
        if(p.isPresent()){
            return p.get();
        }
        else {
            throw new NotFoundException("Property not found!");
        }
    }
}
