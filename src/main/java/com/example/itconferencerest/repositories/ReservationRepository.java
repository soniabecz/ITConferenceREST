package com.example.itconferencerest.repositories;

import com.example.itconferencerest.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findAllByLectureID(Long id);
}
