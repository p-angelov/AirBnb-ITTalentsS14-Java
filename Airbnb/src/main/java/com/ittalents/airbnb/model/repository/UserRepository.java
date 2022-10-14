package com.ittalents.airbnb.model.repository;

import com.ittalents.airbnb.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
