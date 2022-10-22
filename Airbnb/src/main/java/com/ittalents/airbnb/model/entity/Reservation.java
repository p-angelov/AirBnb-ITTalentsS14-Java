package com.ittalents.airbnb.model.entity;

import com.ittalents.airbnb.model.ReservationKey;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity(name = "reservations")
public class Reservation {
    @EmbeddedId
    ReservationKey reservationId;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id")
    User user;

    @ManyToOne
    @MapsId("propertyId")
    @JoinColumn(name = "id")
    Property property;
    @Column
    LocalDate startDate;
    @Column
    LocalDate endDate;
    @Column
    int price;
    @Column
    String paymentType;
}
