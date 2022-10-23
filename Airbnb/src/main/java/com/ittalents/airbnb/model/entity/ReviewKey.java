package com.ittalents.airbnb.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ReviewKey implements Serializable {
    @Column(name = "rev_user_id")
    Long userId;

    @Column(name = "rev_property_id")
    Long propertyId;

    public ReviewKey(long userId, long propertyId) {
        this.userId = userId;
        this.propertyId = propertyId;
    }

    public ReviewKey() {

    }
}
