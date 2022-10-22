package com.ittalents.airbnb.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JoinColumn(name = "guest_id")
    User user;
    @ManyToOne
    @MapsId("propertyId")
    @JoinColumn(name = "res_property_id")
    Property property;
    @Column
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate startDate;
    @Column
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate endDate;
    @Column
    double price;
    @Column
    String paymentType;
}
