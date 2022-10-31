package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.dto.reservationDtos.ReservationCancellationDto;
import com.ittalents.airbnb.model.dto.reservationDtos.ReservationDto;
import com.ittalents.airbnb.model.dto.reservationDtos.ReservationResponseDto;
import com.ittalents.airbnb.model.dto.reservationDtos.ReservationResponseUserDto;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.Reservation;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService extends AbstractService{
    @Autowired
    protected JavaMailSender mailSender;
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
        /*
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("phangelov@gmail.com");
        email.setTo(reservation.getUser().getEmail());
        email.setSubject("Summary for your reservation at " + reservation.getProperty().getName());
        email.setText("You've just made a reservation at " + reservation.getProperty().getName() + " from " + reservation.getStartDate() + " to " + reservation.getEndDate() + "!\n" +
                " The total price is " + reservation.getPrice() + " and you chose payment by " + reservation.getPaymentType() + ". Airbnb wishes you a pleasant stay" + reservation.getUser().getUsername() + "!");
        mailSender.send(email);
         */


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

    public ReservationCancellationDto cancelReservation(long rid){
        Optional<Reservation> opt = reservationRepository.findById(rid);
        if (!opt.isPresent()){
            throw new BadRequestException("There's no such reservation to be cancelled!");
        }
        else {
            ReservationCancellationDto dto = new ReservationCancellationDto();
            dto.setId(opt.get().getId());
            dto.setStartDate(opt.get().getStartDate());
            dto.setEndDate(opt.get().getEndDate());
            dto.setRefundAmount(opt.get().getPrice());
            dto.setRefundType(opt.get().getPaymentType());
            dto.setPropertyId(opt.get().getProperty().getId());
            //todo send cancellation summary by email
            reservationRepository.deleteById(rid);
            return dto;
        }
    }

    public List<ReservationResponseDto> getAllHostReservations(Long uid) {
        User user = userRepository.findById(uid).get();
        if(!user.isHost()){
            throw new BadRequestException("The logged user is not a host");
        }
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < user.getProperties().size(); i++) {
            if(user.getProperties().get(i).getReservations()!=null) {
                reservations.addAll(user.getProperties().get(i).getReservations());
            }
        }
        List<ReservationResponseDto> reservationResponseDtos = reservations.stream().map(reservation -> modelMapper.map(reservation,ReservationResponseDto.class)).collect(Collectors.toList());
        for(ReservationResponseDto reservation:reservationResponseDtos){
            reservation.setTenantUsername(reservationRepository.findById(reservation.getId()).get().getUser().getUsername());
        }
        return reservationResponseDtos;
    }

    public List<ReservationResponseUserDto> getAllUserReservations(Long uid) {
        User user = userRepository.findById(uid).get();

        List<Reservation> reservations = new ArrayList<>(user.getReservations());

        List<ReservationResponseUserDto> reservationResponseDtos = reservations.stream().map(reservation -> modelMapper.map(reservation,ReservationResponseUserDto.class)).collect(Collectors.toList());
        for(ReservationResponseUserDto reservation:reservationResponseDtos){

            reservation.setPropertyName(reservationRepository.findById(reservation.getId()).get().getProperty().getName());
        }
        return reservationResponseDtos;
    }
}
