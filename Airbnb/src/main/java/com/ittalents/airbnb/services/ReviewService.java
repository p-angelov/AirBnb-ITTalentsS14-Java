package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyResponseDto;
import com.ittalents.airbnb.model.entity.ReviewKey;
import com.ittalents.airbnb.model.dto.reservationDtos.reviewDtos.ReviewDto;
import com.ittalents.airbnb.model.dto.reservationDtos.reviewDtos.ReviewResponseDto;
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
        if(dto.getRating()>5 || dto.getRating()<0){
            throw new BadRequestException("Rating value must be from 0 to 5");
        }
        Review review = new Review();
        Property property = getPropertyByIdAs(pid);
        review.setProperty(property);
        review.setUser(getUserById(uid));
        review.setComment(dto.getComment());
        review.setId(new ReviewKey(uid, property.getId()));
        double currentReviewRating = 0;
        currentReviewRating = dto.getRating();
        //calculating and updating rating of reviewed property
        if (getPropertyByIdAs(pid).getRating() == 0){
            review.setRating(currentReviewRating);
            getPropertyByIdAs(pid).setRating(dto.getRating());
        }
        else {
            double rating = (dto.getRating() + getPropertyByIdAs(pid).getRating()) / 2;
            review.setRating(rating);
            getPropertyByIdAs(pid).setRating(rating);
        }

        ReviewResponseDto responseDto = modelMapper.map(review, ReviewResponseDto.class);
        responseDto.setCommenterId(uid);
        responseDto.setRating(currentReviewRating);
        reviewRepository.save(review);
        propertyRepository.save(getPropertyByIdAs(pid));
        return responseDto;
    }

    public PropertyResponseDto deleteReview(long pid){
        return null;
    }
}
