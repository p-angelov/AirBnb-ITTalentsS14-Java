package com.ittalents.airbnb.model.repositories;

import com.ittalents.airbnb.model.entity.Property;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyPagingRepository extends PagingAndSortingRepository<Property,Long> {

    List<Property> findAllByType(String type, Pageable pageable);
    List<Property> findAllByPricePerNightBetween(double minPrice,double maxPrice,Pageable pageable);


}
