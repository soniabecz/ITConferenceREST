package com.example.itconferencerest.controllers;

import com.example.itconferencerest.models.Reservation;
import com.example.itconferencerest.models.Subject;
import com.example.itconferencerest.models.User;
import com.example.itconferencerest.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    ConferenceService service;


    @GetMapping()
    public ResponseEntity<String> getConferencePlan() {
        String conferencePlan = service.getConferencePlan();
        return new ResponseEntity<>(conferencePlan, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers() {
        String users = service.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = service.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/reservations/user")
    public ResponseEntity<List<Reservation>> getAllReservationsByUser(@RequestParam("login") String login) {
        List<Reservation> usersReservation = service.getReservationsByUsersLogin(login);
        return new ResponseEntity<>(usersReservation, HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> saveTodo(@RequestParam("login") String login, @RequestParam("email") String email, @RequestParam("lectureID") Long lectureID) {
        Reservation reservation = service.makeReservation(login, email, lectureID);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    @DeleteMapping({"/reservation/delete/{id}"})
    public ResponseEntity<Reservation> deleteReservation(@PathVariable("id") Long id) {
        service.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping({"/update/email/user/{id}"})
    public ResponseEntity<User> updateEmail(@PathVariable("id") Long id, @RequestParam("email") String email) {
        User user = service.updateEmail(id,email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<User> saveUser(@RequestParam("login") String login, @RequestParam("email") String email) {
        User user = service.saveUser(login,email);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/attendees/lecture/{id}")
    public ResponseEntity<String> getLectureData(@PathVariable Long id) {
        String data = service.getLecturesData(id);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/attendees/subject")
    public ResponseEntity<String> getLectureData(@RequestParam("subject") Subject subject) {
        String data = service.getSubjectsData(subject);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
