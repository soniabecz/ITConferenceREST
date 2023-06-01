package com.example.itconferencerest.service;

import com.example.itconferencerest.models.Lecture;
import com.example.itconferencerest.models.Reservation;
import com.example.itconferencerest.models.Subject;
import com.example.itconferencerest.models.User;
import com.example.itconferencerest.repositories.ReservationRepository;
import com.example.itconferencerest.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ConferenceService {
    UserRepository userRepository;
    ReservationRepository reservationRepository;
    List<Lecture> lectures;

    public ConferenceService(UserRepository userRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    private void addLectures() {
        List<Lecture> lectures = new ArrayList<>();

        lectures.add(new Lecture(0L, new ArrayList<>(), new Date(2023,6,1,10,0), new Date(2023,6,1,11,45), Subject.SUBJECT_1));
        lectures.add(new Lecture(1L, new ArrayList<>(), new Date(2023,6,1,12,0), new Date(2023,6,1,13,45), Subject.SUBJECT_1));
        lectures.add(new Lecture(2L, new ArrayList<>(), new Date(2023,6,1,14,0), new Date(2023,6,1,15,45), Subject.SUBJECT_1));
        lectures.add(new Lecture(3L, new ArrayList<>(), new Date(2023,6,1,10,0), new Date(2023,6,1,11,45), Subject.SUBJECT_2));
        lectures.add(new Lecture(4L, new ArrayList<>(), new Date(2023,6,1,12,0), new Date(2023,6,1,13,45), Subject.SUBJECT_2));
        lectures.add(new Lecture(5L, new ArrayList<>(), new Date(2023,6,1,14,0), new Date(2023,6,1,15,45), Subject.SUBJECT_2));
        lectures.add(new Lecture(6L, new ArrayList<>(), new Date(2023,6,1,10,0), new Date(2023,6,1,11,45), Subject.SUBJECT_3));
        lectures.add(new Lecture(7L, new ArrayList<>(), new Date(2023,6,1,12,0), new Date(2023,6,1,13,45), Subject.SUBJECT_3));
        lectures.add(new Lecture(8L, new ArrayList<>(), new Date(2023,6,1,14,0), new Date(2023,6,1,15,45), Subject.SUBJECT_3));

        this.lectures = lectures;
    }

    public String getConferencePlan() {
        String conferencePlan;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        if(lectures.isEmpty()) {
            addLectures();
        }

        conferencePlan = "Conference Plan:\n";

        for (Lecture lecture:lectures) {
            conferencePlan += simpleDateFormat.format(lecture.getStartTime()) + ":" + simpleDateFormat.format(lecture.getEndTime()) + "\n";
            conferencePlan += lecture.getSubject() + "\n";
        }

        return conferencePlan;
    }

    public List<Reservation> getReservationsByUsersLogin(String login) {
        Optional<User> user = userRepository.findByLogin(login);

        if (user.isPresent()) {
            return user.get().getReservations();
        } else {
            System.out.println("Użytkownik o podanym loginie nie istnieje");
            return Collections.emptyList();
        }
    }

    public void makeReservation(String login, String email, Long lectureID) {
        List<User> allUsers = userRepository.findAll();
        Lecture chosenLecture = null;

        for (User user : allUsers) {
            if (user.getLogin().equals(login) && !user.getEmail().equals(email)) {
                System.out.println("Podany login jest już zajęty");
                return;
            }
        }

        Optional<User> user = userRepository.findByLogin(login);

        List<Reservation> usersReservation = user.get().getReservations();

        Reservation reservation = new Reservation(0L, lectureID, user.get());
        reservationRepository.save(reservation);

        for (Lecture lecture : lectures) {
            if (lecture.getId() == lectureID) {
                chosenLecture = lecture;
            }
        }

        for (Reservation res : usersReservation) {
            if (lectures.get(Math.toIntExact(res.getLectureID())).getStartTime().compareTo(chosenLecture.getStartTime()) != 0) {
                System.out.println("Masz już rezerwację na tę godzinę");
                return;
            }
        }

        if (chosenLecture.getReservations().size() < 5) {
            List<Reservation> reservations = chosenLecture.getReservations();
            reservations.add(reservation);
            chosenLecture.setReservations(reservations);
            user.get().setReservations(reservations);
        } else {
            System.out.println("Na wybraną prelekcję nie ma już miejsc");
        }

        //TODO wysyłanie maili
    }

    public void deleteReservation(Long reservationID, Long userID) {
        Optional<User> user = userRepository.findById(userID);
        Optional<Reservation> reservation = reservationRepository.findById(reservationID);
        Lecture lecture = lectures.get(Math.toIntExact(reservation.get().getLectureID()));
        reservationRepository.deleteById(reservationID);


        List<Reservation> usersReservations = user.get().getReservations();
        usersReservations.remove(reservationID);
        user.get().setReservations(usersReservations);

        List<Reservation> lecturesReservations = reservationRepository.findAllByLectureID(reservation.get().getLectureID());
        usersReservations.remove(reservationID);
        user.get().setReservations(lecturesReservations);
    }
}
