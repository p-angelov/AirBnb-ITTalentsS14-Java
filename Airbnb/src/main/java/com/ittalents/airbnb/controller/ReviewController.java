package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.dto.reservationDtos.ReservationDto;
import com.ittalents.airbnb.model.dto.reviewDtos.ReviewDto;
import com.ittalents.airbnb.model.dto.reviewDtos.ReviewResponseDto;
import com.ittalents.airbnb.model.entity.Review;
import com.ittalents.airbnb.model.repository.PropertyRepository;
import com.ittalents.airbnb.model.repository.ReviewRepository;
import com.ittalents.airbnb.model.repository.UserRepository;
import com.ittalents.airbnb.services.ReviewService;
import com.ittalents.airbnb.util.SessionManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
