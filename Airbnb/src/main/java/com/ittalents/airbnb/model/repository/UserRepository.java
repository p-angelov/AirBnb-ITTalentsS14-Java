package com.ittalents.airbnb.model.repository;

import com.ittalents.airbnb.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List< Optional<User> > findByUsername(String username);
    List<Optional<User> > findByEmail(String email);
}
