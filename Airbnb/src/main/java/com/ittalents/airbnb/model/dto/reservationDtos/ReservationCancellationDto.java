package com.ittalents.airbnb.model.dto.reservationDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationCancellationDto {
    private long id;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;
    private long propertyId;
    private double refundAmount;
    private String refundType;
}
