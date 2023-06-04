package com.example.itconferencerest.controllers;

import com.example.itconferencerest.models.Reservation;
import com.example.itconferencerest.models.Subject;
import com.example.itconferencerest.models.User;
import com.example.itconferencerest.service.ConferenceServiceImpl;
import com.example.itconferencerest.validators.UniqueUsername;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ReservationController {
    ConferenceServiceImpl service;

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = service.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/reservations/user")
    public ResponseEntity<List<Reservation>> getAllReservationsByUser(@RequestParam("login") String login) {
        List<Reservation> usersReservation = service.getReservationsByUsersLogin(login);

        if (usersReservation == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(usersReservation, HttpStatus.OK);
    }

    @PostMapping("/reservations/add")
    public ResponseEntity<Reservation> saveReservation(@Validated(UniqueUsername.class) @RequestBody User user, BindingResult bindingResult, @RequestParam("lectureID") Long lectureID) {
        if (bindingResult.hasErrors())
        {
            bindingResult.getAllErrors().forEach(System.out::println);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Reservation reservation = service.makeReservation(user.getLogin(), user.getEmail(), lectureID);

        if (reservation == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    @DeleteMapping({"/reservation/delete/{id}"})
    public ResponseEntity<Reservation> deleteReservation(@PathVariable("id") Long id) {
        service.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/attendees/lecture/{id}")
    public ResponseEntity<String> getLectureData(@PathVariable Long id) {
        String data = service.getLecturesData(id);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/attendees/subject")
    public ResponseEntity<String> getSubjectData(@RequestParam("subject") Subject subject) {
        String data = service.getSubjectsData(subject);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
