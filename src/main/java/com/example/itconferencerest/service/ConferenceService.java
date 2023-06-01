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
    List<Lecture> lectures = new ArrayList<>();

    public ConferenceService(UserRepository userRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    private void addLectures() {

        lectures.add(new Lecture(0L, new ArrayList<>(), new Date(2023-1900, Calendar.JUNE,1,10,0), new Date(2023-1900, Calendar.JUNE,1,11,45), Subject.SUBJECT_1));
        lectures.add(new Lecture(1L, new ArrayList<>(), new Date(2023-1900, Calendar.JUNE,1,12,0), new Date(2023-1900, Calendar.JUNE,1,13,45), Subject.SUBJECT_1));
        lectures.add(new Lecture(2L, new ArrayList<>(), new Date(2023-1900, Calendar.JUNE,1,14,0), new Date(2023-1900, Calendar.JUNE,1,15,45), Subject.SUBJECT_1));
        lectures.add(new Lecture(3L, new ArrayList<>(), new Date(2023-1900, Calendar.JUNE,1,10,0), new Date(2023-1900, Calendar.JUNE,1,11,45), Subject.SUBJECT_2));
        lectures.add(new Lecture(4L, new ArrayList<>(), new Date(2023-1900, Calendar.JUNE,1,12,0), new Date(2023-1900, Calendar.JUNE,1,13,45), Subject.SUBJECT_2));
        lectures.add(new Lecture(5L, new ArrayList<>(), new Date(2023-1900, Calendar.JUNE,1,14,0), new Date(2023-1900, Calendar.JUNE,1,15,45), Subject.SUBJECT_2));
        lectures.add(new Lecture(6L, new ArrayList<>(), new Date(2023-1900, Calendar.JUNE,1,10,0), new Date(2023-1900, Calendar.JUNE,1,11,45), Subject.SUBJECT_3));
        lectures.add(new Lecture(7L, new ArrayList<>(), new Date(2023-1900, Calendar.JUNE,1,12,0), new Date(2023-1900, Calendar.JUNE,1,13,45), Subject.SUBJECT_3));
        lectures.add(new Lecture(8L, new ArrayList<>(), new Date(2023-1900, Calendar.JUNE,1,14,0), new Date(2023-1900, Calendar.JUNE,1,15,45), Subject.SUBJECT_3));

    }

    public String getConferencePlan() {
        String conferencePlan;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        if(lectures == null || lectures.isEmpty()) {
            addLectures();
        }

        conferencePlan = "Conference Plan:\n";

        for (Lecture lecture:lectures) {
            conferencePlan += simpleDateFormat.format(lecture.getStartTime()) + " to " + simpleDateFormat.format(lecture.getEndTime()) + " ";
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

    public Reservation makeReservation(String login, String email, Long lectureID) {

        Lecture chosenLecture = new Lecture();

        if(lectures.isEmpty()) {
            addLectures();
        }

        User user = saveUser(login,email);

        if (user == null) {
            return null;
        }

        chosenLecture = findLectureById(lectureID);

        if (!checkIfUserHasTime(user,chosenLecture.getStartTime())) {
            return null;
        }

        int attendeesNo = chosenLecture.getReservations().size()+1;

        Reservation reservation = new Reservation(0L, lectureID, user);

        if (attendeesNo < 5) {
            user.setReservations(reservationRepository.findAllByUser_Id(user.getId()));
            chosenLecture.setReservations(reservationRepository.findAllByLectureID(lectureID));
            return reservationRepository.save(reservation);
        } else {
            System.out.println("Na wybraną prelekcję nie ma już miejsc");
            return null;
        }

        //TODO wysyłanie maili

    }

    public void deleteReservation(Long reservationID) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationID);
        Optional<User> user = userRepository.findByReservationsContains(reservation.get());
        Lecture lecture = lectures.get(Math.toIntExact(reservation.get().getLectureID()));
        reservationRepository.deleteById(reservationID);

        user.get().setReservations(reservationRepository.findAllByUser_Id(user.get().getId()));

        lecture.setReservations(reservationRepository.findAllByLectureID(lecture.getId()));
    }

    public User updateEmail(Long userID, String newEmail) {
        Optional<User> user = userRepository.findById(userID);
        user.get().setEmail(newEmail);
        userRepository.save(user.get());

        return user.get();
    }

    public String getAllUsers() {
        List<User> users = userRepository.findAll();
        String usersList;

        usersList = "Users:\n";

        for (User user : users) {
            usersList += "Login: " + user.getLogin() + " Email: " + user.getEmail() + "\n";
        }

        return usersList;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations;
    }

    public User saveUser(String login, String email) {
        List<User> users = userRepository.findAll();
        User user = new User(0L,login,email,new ArrayList<>());

        for (User u : users) {
            if (u.getLogin().equals(login) && !u.getEmail().equals(email)) {
                System.out.println("Podany login jest już zajęty");
                return null;
            }
            if (u.getLogin().equals(login) && u.getEmail().equals(email)) {
                return userRepository.findByLogin(login).get();
            }
        }

        return userRepository.save(user);
    }

    public Lecture findLectureById(Long id) {
        Lecture chosenLecture = new Lecture();

        for (Lecture lecture : lectures) {
            if (lecture.getId() == id) {
                chosenLecture = lecture;
            }
        }

        return chosenLecture;
    }

    public boolean checkIfUserHasTime(User user, Date time) {

        List<Reservation> usersReservation = user.getReservations();

        if (!usersReservation.isEmpty()) {
            for (Reservation res : usersReservation) {
                if (lectures.get(Math.toIntExact(res.getLectureID())).getStartTime().compareTo(time) == 0) {
                    System.out.println("Masz już rezerwację na tę godzinę");
                    return false;
                }
            }
        }
        return true;
    }

    public String getLecturesData(Long lectureID) {
        String lectureData;

        List<Reservation> reservations = reservationRepository.findAllByLectureID(lectureID);
        List<User> users = userRepository.findAll();

        double lectureAttendeesNo = reservations.size();
        double allAttendeesNo = users.size();
        double percentage = (lectureAttendeesNo/allAttendeesNo)*100;

        lectureData = "This lecture was attended by " + lectureAttendeesNo + " of " + allAttendeesNo + " attendees which is " + percentage + "%";

        return lectureData;
    }

    public String getSubjectsData(Subject subject) {
        String subjectsData;
        List<Reservation> allReservations = reservationRepository.findAll();
        List<Reservation> reservationsBySubject = new ArrayList<>();

        for (Reservation reservation : allReservations) {
            if (lectures.get(Math.toIntExact(reservation.getLectureID())).getSubject().equals(subject)) {
                reservationsBySubject.add(reservation);
            }
        }

        double subjectReservationsNo = reservationsBySubject.size();
        double allReservationsNo = allReservations.size();
        double percentage = (subjectReservationsNo/allReservationsNo)*100;

        subjectsData = "This subject was chosen for " + subjectReservationsNo + " of " + allReservationsNo + " reservations which is " + percentage + "%";

        return subjectsData;
    }

}
