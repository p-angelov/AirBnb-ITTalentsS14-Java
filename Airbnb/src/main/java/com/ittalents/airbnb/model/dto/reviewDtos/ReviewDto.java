package com.ittalents.airbnb.model.dto.reviewDtos;

import com.ittalents.airbnb.model.dto.userDTOs.UserResponseDto;
import lombok.Data;

@Data
public class ReviewDto {

    private double rating;
    private String comment;
    private long propertyId;
}
