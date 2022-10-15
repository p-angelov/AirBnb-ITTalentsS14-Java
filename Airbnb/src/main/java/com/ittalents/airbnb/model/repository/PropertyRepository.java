package com.ittalents.airbnb.model.repository;

import com.ittalents.airbnb.model.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

}
