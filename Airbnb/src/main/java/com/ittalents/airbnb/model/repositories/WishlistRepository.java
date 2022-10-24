package com.ittalents.airbnb.model.repositories;

import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<User, Property> {
}
