package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.dto.reservationDtos.ReservationDto;
import com.ittalents.airbnb.model.repositories.UserRepository;
import com.ittalents.airbnb.services.ReservationService;
import com.ittalents.airbnb.services.UserService;
import com.ittalents.airbnb.util.SessionManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@RestController
public class ReservationController extends MasterController{
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ReservationService reservationService;
    @PostMapping("/reservation")
    public ReservationDto makeReservation(@RequestBody ReservationDto dto, HttpServletRequest request){
        SessionManager.validateLogin(request);
        reservationService.makeReservation(dto,(Long)request.getSession().getAttribute(SessionManager.USER_ID));
        return dto;
    }
}
