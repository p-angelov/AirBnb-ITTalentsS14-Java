package com.ittalents.airbnb.model.dto.reservationDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class ReservationResponseUserDto {
    private Long id;
    private String propertyName;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;
    private long propertyId;
    private String paymentType;
    private double price;

}
