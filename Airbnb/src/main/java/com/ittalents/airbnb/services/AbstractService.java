package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.dao.PropertyDao;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.security.spec.NamedParameterSpec;
import java.util.Optional;

public abstract class AbstractService {

    @Autowired
    protected PropertyDao propertyDao;
    @Autowired
    protected  PropertyPagingRepository propertyPagingRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected PropertyRepository propertyRepository;
    @Autowired
    protected PhotoRepository photoRepository;
    @Autowired
    protected ReviewRepository reviewRepository;
    @Autowired
    protected ReservationRepository reservationRepository;
    @Autowired
    protected ModelMapper modelMapper;
    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    public User getUserById(long id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            throw new NotFoundException("There is no user with id " + id + "!");
        }
    }

    public Property getPropertyByIdAs(long id){
        if(propertyRepository.findById(id).isPresent()){
            return propertyRepository.findById(id).get();
        }
        else{
            throw  new NotFoundException("There is no property with such id!");
        }
    }

    public void deleteUserById(Long id){
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            userRepository.delete(opt.get());
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public void deletePropertyById(Long id){
        Optional<Property> opt = propertyRepository.findById(id);
        if (opt.isPresent()) {
            propertyRepository.delete(opt.get());
        } else {
            throw new NotFoundException("Property not found!");
        }
    }

    public void validatePhoto(MultipartFile file) {
        if (!file.getContentType().startsWith("image/")) {
            throw new BadRequestException("Invalid file format! Please upload an image!");
        }
    }
}
