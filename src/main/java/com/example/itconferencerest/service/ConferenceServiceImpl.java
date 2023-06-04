package com.example.itconferencerest.service;

import com.example.itconferencerest.models.Lecture;
import com.example.itconferencerest.models.Reservation;
import com.example.itconferencerest.models.Subject;
import com.example.itconferencerest.models.User;
import com.example.itconferencerest.repositories.ReservationRepository;
import com.example.itconferencerest.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ConferenceServiceImpl implements ConferenceService {

    UserRepository userRepository;

    ReservationRepository reservationRepository;
    List<Lecture> lectures = new ArrayList<>();

    public ConferenceServiceImpl(UserRepository userRepository, ReservationRepository reservationRepository) {
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
            System.out.println("User with given login doesn't exist");
            return null;
        }
    }


    public Reservation makeReservation(String login, String email, Long lectureID) {

        try {

            Lecture chosenLecture = new Lecture();

            if (lectures.isEmpty()) {
                addLectures();
            }

            User user = saveUser(login, email);

            if (user == null) {
                return null;
            }

            chosenLecture = findLectureById(lectureID);

            if (!checkIfUserHasTime(user, chosenLecture.getStartTime())) {
                return null;
            }

            int attendeesNo = chosenLecture.getReservations().size() + 1;

            Reservation reservation = new Reservation(0L, lectureID, user);

            if (attendeesNo < 5) {
                user.setReservations(reservationRepository.findAllByUser_Id(user.getId()));
                chosenLecture.setReservations(reservationRepository.findAllByLectureID(lectureID));
                String mail = "Hello " + login + ", your reservation for lecture on " + chosenLecture.getSubject() + " taking place from " + chosenLecture.getStartTime().toString() + " to " + chosenLecture.getEndTime().toString() + " was successfully made";
                sendEmail(mail, email);
                return reservationRepository.save(reservation);
            } else {
                System.out.println("Chosen lecture is already full");
                return null;
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return null;
        }

    }

    public void deleteReservation(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isPresent()) {
            Lecture lecture = lectures.get(Math.toIntExact(reservation.get().getLectureID()));
            reservationRepository.deleteById(id);

            lecture.setReservations(reservationRepository.findAllByLectureID(lecture.getId()));
        }
    }

    public User updateEmail(Long userID, String newEmail) {
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()) {
            user.get().setEmail(newEmail);
            userRepository.save(user.get());
            return user.get();
        } else {
            System.out.println("User doesn't exist");
            return null;
        }
    }

    public String getAllUsers() {
        List<User> users = userRepository.findAll();
        StringBuilder usersList;

        usersList = new StringBuilder("Users:\n");

        for (User user : users) {
            usersList.append("Login: ").append(user.getLogin()).append(" Email: ").append(user.getEmail()).append("\n");
        }

        return usersList.toString();
    }

    public List<Reservation> getAllReservations() {

        return reservationRepository.findAll();
    }


    public User saveUser(String login, String email) {
        List<User> users = userRepository.findAll();
        User user = new User(0L,login,email,new ArrayList<>());

        for (User u : users) {
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
                    System.out.println("You already have a reservation at this time");
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

        lectureData = "This lecture was attended by " + lectureAttendeesNo + " of " + allAttendeesNo + " attendees which is " + String.format("%.2f",percentage) + "%";

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

        subjectsData = "This subject was chosen for " + subjectReservationsNo + " of " + allReservationsNo + " reservations which is " + String.format("%.2f",percentage) + "%";

        return subjectsData;
    }

    public void sendEmail(String mail, String recipient) {
        try {
            File file = new File("src/main/resources/static/powiadomienia.txt");
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter("src/main/resources/static/powiadomienia.txt");
                writer.write("Date | Mail | Recipient\n");
                writer.write(new Date().toString() + " | " + mail + " | " + recipient + "\n");
                writer.close();

            } else {
                FileWriter writer = new FileWriter("src/main/resources/static/powiadomienia.txt", true);
                writer.write(new Date().toString() + " | " + mail + " | " + recipient + "\n");
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfLoginAlreadyExists(User user) {

        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();

        if (!users.isEmpty()) {
            Optional<User> userByLogin = userRepository.findByLogin(user.getLogin());

            if (userByLogin.isPresent()) {
                for (User u : users) {
                    if (u.getLogin().equals(user.getLogin()) && !u.getEmail().equals(user.getEmail())) {
                        System.out.println("Podany login jest już zajęty");
                        return false;
                    }
                }
            }
        } else {
            return true;
        }
        return true;
    }
}
