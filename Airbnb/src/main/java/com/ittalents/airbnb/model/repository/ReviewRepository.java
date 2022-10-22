package com.ittalents.airbnb.model.repository;

import com.ittalents.airbnb.model.ReservationKey;
import com.ittalents.airbnb.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReservationKey> {

}