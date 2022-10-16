package com.ittalents.airbnb.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@Entity(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //todo initialize host_id to newly created properties
    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    @Column
    private String name;
    @Column
    private String description;
    @Column
    private int maxGuests;
    @Column
    private int size;
    @Column
    private double pricePerNight;
    @Column
    private String photo;
    @Column
    private String type;
    @Column
    private int bathrooms;
    @Column
    private int beds;
    @Column
    private double rating;
    @JsonFormat(pattern = "HH:mm")
    @Column
    private LocalTime timeOfArrival;
    @JsonFormat(pattern = "HH:mm")
    @Column
    private LocalTime leavingTime;
    @Column
    private long extras; // extras with bit representation
    //todo bitwise operations with extras
}
