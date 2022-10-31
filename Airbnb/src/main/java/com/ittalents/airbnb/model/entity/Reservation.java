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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "res_property_id")
    private Property property;
    @Column
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;
    @Column
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;
    @Column
    private double price;
    @Column
    private String paymentType;
}
