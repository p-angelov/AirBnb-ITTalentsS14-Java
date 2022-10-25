package com.ittalents.airbnb.model.repositories;

import com.ittalents.airbnb.model.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
   //  @Query("DELETE FROM properties WHERE host_id = ?1")
    void deletePropertyById(long pid);
}
