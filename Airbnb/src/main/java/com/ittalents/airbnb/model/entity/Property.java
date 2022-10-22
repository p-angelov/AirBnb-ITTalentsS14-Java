package com.ittalents.airbnb.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@Entity(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
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

    @OneToMany(mappedBy = "property")
    private List<Photo> propertyPhotos;

    @OneToOne(mappedBy = "property",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Address address;

    @Column
    private String type;
    @Column
    private int bathrooms;
    @Column
    private int beds;
    @Column
    private double rating;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column
    private LocalTime timeOfArrival;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column
    private LocalTime leavingTime;

    @Column
    private long extras; // extras with bit representation
    @OneToMany(mappedBy = "property")
    Set<Reservation> reservations;
}
