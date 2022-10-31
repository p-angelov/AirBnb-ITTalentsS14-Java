package com.ittalents.airbnb.model.dto.reservationDtos.reviewDtos;

import com.ittalents.airbnb.model.dto.userDTOs.UserResponseDto;
import lombok.Data;

@Data
public class ReviewResponseDto {

    private double rating;
    private String comment;
    private Long commenterId;
}
