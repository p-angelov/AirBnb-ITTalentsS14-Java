package com.ittalents.airbnb.model.entity;

import com.ittalents.airbnb.model.ReviewKey;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "reviews")
public class Review {

    @EmbeddedId
    ReviewKey reviewId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "rev_user_id")
    private User user;

    @ManyToOne
    @MapsId("propertyId")
    @JoinColumn(name = "rev_property_id")
    private Property property;

    @Column
    private double rating;
    @Column
    private String comment;

}
