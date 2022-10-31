package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.dto.propertyDTOs.GeneralPropertyResponseDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.PagePropertyDto;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService extends AbstractService{


    public GeneralPropertyResponseDto addToWishlist(long pid, long uid) {
        Property property = getPropertyByIdAs(pid);
        User user = getUserById(uid);
        if (user.getWishlist().contains(property)){
            user.getWishlist().remove(property);
        }
        else{
            user.getWishlist().add(property);
        }
        userRepository.save(user);
        return modelMapper.map(property, GeneralPropertyResponseDto.class);
    }

    public PagePropertyDto removeFromWishlist(long pid, Long uid) {
        Property property = getPropertyByIdAs(pid);
        User user = getUserById(uid);
        user.getWishlist().remove(property);
        userRepository.save(user);
        return modelMapper.map(property, PagePropertyDto.class);
    }

    public List<PagePropertyDto> getAllFromWishlist(Long uid) {
        User u = getUserById(uid);
        return u.getWishlist().stream().map(property -> modelMapper.map(property, PagePropertyDto.class) ).collect(Collectors.toList());
    }


}
