package com.example.itconferencerest.repositories;

import com.example.itconferencerest.models.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture,Long> {
}
