package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.dto.reservationDtos.ReservationDto;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.Reservation;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService extends AbstractService{
    public void makeReservation(ReservationDto dto,long id){
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
        checkAvailability(dto);
        Reservation reservation = new Reservation();
        reservation.setUser(getUserById(id));
        reservation.setProperty(property);
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        int days = (int)ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        double price = days* property.getPricePerNight();
        reservation.setPrice(price);
        reservation.setPaymentType(dto.getPaymentType());
        reservationRepository.save(reservation);
    }
    public void checkAvailability(ReservationDto dto){
        List<Reservation> reservationsForProperty = reservationRepository.findAllByPropertyId(dto.getPropertyId());
        for(Reservation reservation:reservationsForProperty){
            if((dto.getStartDate().isAfter(reservation.getStartDate()) && dto.getStartDate().isBefore(reservation.getEndDate()))||
                    (dto.getEndDate().isAfter(reservation.getStartDate()) && dto.getEndDate().isBefore(reservation.getEndDate()))||
                    ((dto.getStartDate().isBefore(reservation.getStartDate())||dto.getStartDate().isEqual(reservation.getStartDate()))
                            &&( dto.getEndDate().isAfter(reservation.getEndDate())||dto.getEndDate().isEqual(reservation.getEndDate())))||
                    (dto.getStartDate().isEqual(reservation.getStartDate())&&dto.getEndDate().isEqual(reservation.getEndDate()))
            ){
                throw new BadRequestException("These days are already reserved");
            }
        }
    }
}
