package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.repository.PhotoRepository;
import com.ittalents.airbnb.model.repository.PropertyRepository;
import com.ittalents.airbnb.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractService {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected PropertyRepository propertyRepository;
    @Autowired
    protected PhotoRepository photoRepository;
    @Autowired
    protected ModelMapper modelMapper;
    public User getUserById(long id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            throw new NotFoundException("There is no user with id " + id);
        }
    }


}
