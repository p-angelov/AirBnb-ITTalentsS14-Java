package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyResponseDto;
import com.ittalents.airbnb.model.dto.reservationDtos.reviewDtos.ReviewDto;
import com.ittalents.airbnb.model.dto.reservationDtos.reviewDtos.ReviewResponseDto;
import com.ittalents.airbnb.model.repositories.PropertyRepository;
import com.ittalents.airbnb.model.repositories.ReviewRepository;
import com.ittalents.airbnb.model.repositories.UserRepository;
import com.ittalents.airbnb.services.ReviewService;
import com.ittalents.airbnb.util.SessionManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ReviewController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    @PostMapping("properties/review/{pid}")
    public ReviewResponseDto writeReview(@PathVariable long pid, @RequestBody ReviewDto dto, HttpServletRequest request){
        SessionManager.validateLogin(request);
        return reviewService.writeReview(pid, dto, (Long)request.getSession().getAttribute(SessionManager.USER_ID));
    }
    @DeleteMapping("properties/review/{pid}")
    public PropertyResponseDto deleteReview(@PathVariable long pid, HttpServletRequest request){
        SessionManager.validateLogin(request);
        return reviewService.deleteReview(pid);
    }
}
