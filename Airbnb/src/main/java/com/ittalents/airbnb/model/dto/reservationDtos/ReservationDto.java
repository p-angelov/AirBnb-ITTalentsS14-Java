package com.ittalents.airbnb.model.dto.reservationDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyResponseDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDto {
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate endDate;
    long propertyId;
    String paymentType;


}
