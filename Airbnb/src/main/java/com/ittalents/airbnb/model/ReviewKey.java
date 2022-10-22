package com.ittalents.airbnb.model;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode
@Embeddable
public class ReviewKey implements Serializable {

    @Column(name = "rev_user_id")
    long userId;

    @Column(name = "rev_property_id")
    long propertyId;
}
