package com.ittalents.airbnb.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity(name = "reservations")
@EqualsAndHashCode(exclude = "user")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    User user;
    @ManyToOne
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
