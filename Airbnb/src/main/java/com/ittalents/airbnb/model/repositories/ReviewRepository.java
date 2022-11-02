package com.ittalents.airbnb.model.repositories;

import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.Review;
import com.ittalents.airbnb.model.entity.ReviewKey;
import com.ittalents.airbnb.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewKey> {

    Optional<Review> findByUserAndAndProperty(User user, Property property);
}
