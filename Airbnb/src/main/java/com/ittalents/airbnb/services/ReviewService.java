package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.entity.ReviewKey;
import com.ittalents.airbnb.model.dto.reviewDtos.ReviewDto;
import com.ittalents.airbnb.model.dto.reviewDtos.ReviewResponseDto;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.Review;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class ReviewService extends AbstractService{

    public ReviewResponseDto writeReview(long pid, ReviewDto dto, long uid) {
        if (dto.getComment().isBlank()){
            throw new BadRequestException("Invalid comment!");
        }
        Review review = new Review();
        Property property = getPropertyByIdAs(pid);
        review.setProperty(property);
        review.setUser(getUserById(uid));
        review.setComment(dto.getComment());
        review.setId(new ReviewKey(uid, property.getId()));
        //calculating and updating rating of reviewed property
        if (getPropertyByIdAs(dto.getPropertyId()).getRating() == 0){
            review.setRating(dto.getRating());
        }
        else {
            double rating = (dto.getRating() + getPropertyByIdAs(dto.getPropertyId()).getRating()) / 2;
            review.setRating(rating);
        }

        ReviewResponseDto responseDto = modelMapper.map(review, ReviewResponseDto.class);
        reviewRepository.save(review);
        return responseDto;
    }
}
