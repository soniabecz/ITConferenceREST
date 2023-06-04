package com.example.itconferencerest.repositories;

import com.example.itconferencerest.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findAllByLectureID(Long id);
    List<Reservation> findAllByUser_Id(Long id);
}
