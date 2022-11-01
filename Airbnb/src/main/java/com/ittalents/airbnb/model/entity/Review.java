package com.ittalents.airbnb.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity(name = "reviews")
@EqualsAndHashCode(exclude = "user")
public class Review {

    @EmbeddedId
    ReviewKey id;
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
