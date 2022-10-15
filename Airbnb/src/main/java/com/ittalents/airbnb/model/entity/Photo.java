package com.ittalents.airbnb.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "p_property_id")
    private Property property;

    @Column
    private String photoUrl;

}
