package com.ittalents.airbnb.model.repository;

import com.ittalents.airbnb.model.entity.ReservationKey;
import com.ittalents.airbnb.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, ReservationKey> {
    List<Reservation> findAllByPropertyId(long id);
    List<Reservation > findAll();

}
