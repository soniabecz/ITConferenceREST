package com.example.itconferencerest.service;

import com.example.itconferencerest.models.Lecture;
import com.example.itconferencerest.models.Subject;
import com.example.itconferencerest.repositories.ReservationRepository;
import com.example.itconferencerest.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConferenceService {
    UserRepository userRepository;
    ReservationRepository reservationRepository;
    List<Lecture> lectures;

    public ConferenceService(UserRepository userRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    public void addLectures() {
        List<Lecture> lectures = new ArrayList<>();

        lectures.add(new Lecture(11L, new ArrayList<>(), new Date(2023,6,1,10,0), new Date(2023,6,1,11,45), Subject.SUBJECT_1));
        lectures.add(new Lecture(12L, new ArrayList<>(), new Date(2023,6,1,12,0), new Date(2023,6,1,13,45), Subject.SUBJECT_1));
        lectures.add(new Lecture(13L, new ArrayList<>(), new Date(2023,6,1,14,0), new Date(2023,6,1,15,45), Subject.SUBJECT_1));
        lectures.add(new Lecture(21L, new ArrayList<>(), new Date(2023,6,1,10,0), new Date(2023,6,1,11,45), Subject.SUBJECT_2));
        lectures.add(new Lecture(22L, new ArrayList<>(), new Date(2023,6,1,12,0), new Date(2023,6,1,13,45), Subject.SUBJECT_2));
        lectures.add(new Lecture(23L, new ArrayList<>(), new Date(2023,6,1,14,0), new Date(2023,6,1,15,45), Subject.SUBJECT_2));
        lectures.add(new Lecture(31L, new ArrayList<>(), new Date(2023,6,1,10,0), new Date(2023,6,1,11,45), Subject.SUBJECT_3));
        lectures.add(new Lecture(32L, new ArrayList<>(), new Date(2023,6,1,12,0), new Date(2023,6,1,13,45), Subject.SUBJECT_3));
        lectures.add(new Lecture(33L, new ArrayList<>(), new Date(2023,6,1,14,0), new Date(2023,6,1,15,45), Subject.SUBJECT_3));

        this.lectures = lectures;
    }
}
