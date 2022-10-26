package com.ittalents.airbnb.model.repositories;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.ittalents.airbnb.model.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
   //  @Query("DELETE FROM properties WHERE host_id = ?1")
    void deletePropertyById(long pid);

}
