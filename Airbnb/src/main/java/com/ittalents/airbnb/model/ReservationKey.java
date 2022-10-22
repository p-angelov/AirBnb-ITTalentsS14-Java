package com.ittalents.airbnb.model;

import clojure.lang.IFn;
import com.ittalents.airbnb.model.entity.Reservation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.io.Serializable;
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
//@Table(name = "reservations")
public class ReservationKey implements Serializable {
    @Column(name = "guest_id")
    private Long userId;
    @Column(name = "res_property_id")
    private Long propertyId;
    public ReservationKey(Long userId,Long propertyId){
        this.userId= userId;
        this.propertyId = propertyId;
    }

    public ReservationKey() {

    }
}
