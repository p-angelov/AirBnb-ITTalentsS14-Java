package com.ittalents.airbnb.model.repositories;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.ittalents.airbnb.model.entity.Property;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
   //  @Query("DELETE FROM properties WHERE host_id = ?1")
    void deletePropertyById(long pid);

    List<Property> findAllByPricePerNightBetween(double minPrice, double maxPrice);
    List<Property> findAllByType(String type);
    List<Property> findPropertiesByAddress_CityAndAddress_Country(String city, String country);
    List<Property> findPropertiesByAddress_City(String city);
    List<Property> findPropertiesByAddress_Country(String country);




}
