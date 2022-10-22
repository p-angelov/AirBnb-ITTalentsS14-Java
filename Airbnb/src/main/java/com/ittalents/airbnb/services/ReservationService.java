package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.ReservationKey;
import com.ittalents.airbnb.model.dto.reservationDtos.ReservationDto;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.Reservation;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService extends AbstractService{
    @Autowired
    NamedParameterJdbcTemplate dbManager;
    public void makeReservation(ReservationDto dto,long id){
        //todo validations
        if(dto.getStartDate().isBefore(LocalDate.now())){
            throw new BadRequestException("The start date is not valid");
        }
        if(dto.getEndDate().isBefore(LocalDate.now())){
            throw new BadRequestException("The start date is not valid");
        }
        if(dto.getStartDate().isAfter(dto.getEndDate())){
                throw new BadRequestException("The start date must be before end date ");
        }

        Property property = getPropertyByIdAs(dto.getPropertyId());
        List<Reservation> reservationsForProperty = reservationRepository.findAllByPropertyId(dto.getPropertyId());
        System.out.println(reservationsForProperty.size());
        for(Reservation reservation:reservationsForProperty){
            if((dto.getStartDate().isAfter(reservation.getStartDate()) && dto.getStartDate().isBefore(reservation.getEndDate()))||
                    (dto.getEndDate().isAfter(reservation.getStartDate()) && dto.getEndDate().isBefore(reservation.getEndDate()))
            ){
                throw new BadRequestException("These days are already reserved");
            }
        }
        Reservation reservation = new Reservation();
        reservation.setUser(getUserById(id));
        reservation.setProperty(property);
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setReservationId(new ReservationKey(id, property.getId()));
        for(Reservation reservation1:reservationRepository.findAll()){
            if((long)reservation.getReservationId().getPropertyId() == (long)reservation1.getReservationId().getPropertyId() &&
                    (long)reservation.getReservationId().getUserId() == (long)reservation1.getReservationId().getUserId()){
                throw new BadRequestException("Same composite key");
            }
        }
        int days = (int)ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        double price = days* property.getPricePerNight();
        reservation.setPrice(price);
        reservation.setPaymentType(dto.getPaymentType());
        reservationRepository.save(reservation);
    }
}
