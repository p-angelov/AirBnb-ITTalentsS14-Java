package com.ittalents.airbnb.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "properties")
//@EqualsAndHashCode(exclude = "host")
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
    @JsonManagedReference
    @OneToMany(mappedBy = "property",cascade = CascadeType.ALL)
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
    @OneToMany(mappedBy = "property",cascade = CascadeType.ALL)
    Set<Reservation> reservations;

    @OneToMany(mappedBy = "property",cascade = CascadeType.ALL)
    List<Review> reviews;

    @ManyToMany(mappedBy = "wishlist",cascade = CascadeType.ALL)
    Set<User> addedToWishlist;
}
