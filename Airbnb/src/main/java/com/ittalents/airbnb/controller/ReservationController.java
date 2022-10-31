package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.dto.reservationDtos.ReservationCancellationDto;
import com.ittalents.airbnb.model.dto.reservationDtos.ReservationDto;
import com.ittalents.airbnb.model.dto.reservationDtos.ReservationResponseDto;
import com.ittalents.airbnb.model.dto.reservationDtos.ReservationResponseUserDto;
import com.ittalents.airbnb.model.repositories.UserRepository;
import com.ittalents.airbnb.services.ReservationService;
import com.ittalents.airbnb.services.UserService;
import com.ittalents.airbnb.util.SessionManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @DeleteMapping("/reservation/{rid}")
    public ReservationCancellationDto cancelReservation(@PathVariable long rid, HttpServletRequest request){
        SessionManager.validateLogin(request);
        return reservationService.cancelReservation(rid);
    }
    @GetMapping(value = "/users/reservations/as_host")
    public List<ReservationResponseDto> getAllReservationsOfHost (HttpServletRequest request){
        SessionManager.validateLogin(request);
        return reservationService.getAllHostReservations((Long) request.getSession().getAttribute(SessionManager.USER_ID));
    }

    @GetMapping(value = "/users/reservations/as_user")
    public List<ReservationResponseUserDto> getAllReservationsOfTenant (HttpServletRequest request){
        SessionManager.validateLogin(request);
        return reservationService.getAllUserReservations((Long) request.getSession().getAttribute(SessionManager.USER_ID));
    }
}
