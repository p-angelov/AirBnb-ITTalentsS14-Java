package com.ittalents.airbnb.model.repositories;

import com.ittalents.airbnb.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List< Optional<User> > findByUsername(String username);
    List<Optional<User> > findByEmail(String email);
    Optional<User> findUserByUsernameAndPassword(String username,String password);
    void deleteUserById(Long uid);

}
