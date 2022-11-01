package com.ittalents.airbnb.model.dto.reservationDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyResponseDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDto {

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;
    private long propertyId;
    private String paymentType;
}
