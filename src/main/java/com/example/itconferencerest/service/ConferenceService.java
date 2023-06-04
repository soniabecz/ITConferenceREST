package com.example.itconferencerest.service;

import com.example.itconferencerest.models.Lecture;
import com.example.itconferencerest.models.Reservation;
import com.example.itconferencerest.models.Subject;
import com.example.itconferencerest.models.User;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.List;

public interface ConferenceService {
    public String getConferencePlan();
    public List<Reservation> getReservationsByUsersLogin(String login);
    public Reservation makeReservation(String login, String email, Long lectureID);
    public void deleteReservation(Long id);
    public User updateEmail(Long userID, String newEmail);
    public String getAllUsers();
    public List<Reservation> getAllReservations();
    public User saveUser(String login, String email);
    public Lecture findLectureById(Long id);
    public boolean checkIfUserHasTime(User user, Date time);
    public String getLecturesData(Long lectureID);
    public String getSubjectsData(Subject subject);
    public void sendEmail(String mail, String recipient);
    public boolean checkIfLoginAlreadyExists(User user);
}
