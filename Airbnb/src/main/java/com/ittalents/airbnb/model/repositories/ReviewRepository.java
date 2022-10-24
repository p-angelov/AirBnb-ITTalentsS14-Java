package com.ittalents.airbnb.model.repositories;

import com.ittalents.airbnb.model.entity.Review;
import com.ittalents.airbnb.model.entity.ReviewKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewKey> {
}
