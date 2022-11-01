package com.ittalents.airbnb.model.dto.reservationDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class ReservationResponseDto {

    private Long id;
    private String tenantUsername;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;
    private long propertyId;
    private String paymentType;
    private double price;
}
