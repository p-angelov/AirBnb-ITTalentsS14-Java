package com.ittalents.airbnb.model.repository;

import com.ittalents.airbnb.model.ReservationKey;
import com.ittalents.airbnb.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, ReservationKey> {

}
