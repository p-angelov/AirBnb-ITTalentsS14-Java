package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.ReservationKey;
import com.ittalents.airbnb.model.dto.reservationDtos.ReservationDto;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
@Service
public class ReservationService extends AbstractService{

    public void makeReservation(ReservationDto dto,long id){
        Property property = getPropertyByIdAs(dto.getPropertyId());
        Reservation reservation = new Reservation();
        reservation.setUser(getUserById(id));
        reservation.setProperty(property);
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setReservationId(new ReservationKey(property.getId(), id));
        int days = (int)ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        double price = days* property.getPricePerNight();
        reservation.setPrice(price);
        reservation.setPaymentType(dto.getPaymentType());
        reservationRepository.save(reservation);
    }
}
