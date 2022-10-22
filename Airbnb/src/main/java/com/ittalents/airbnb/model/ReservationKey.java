package com.ittalents.airbnb.model;

import clojure.lang.IFn;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.io.Serializable;
@EqualsAndHashCode
@Embeddable
public class ReservationKey implements Serializable {
    @Column(name = "guest_id")
    Long userId;
    @Column(name = "res_property_id")
    Long propertyId;
}
