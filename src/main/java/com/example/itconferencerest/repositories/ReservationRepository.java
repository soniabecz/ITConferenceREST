package com.example.itconferencerest.repositories;

import com.example.itconferencerest.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
