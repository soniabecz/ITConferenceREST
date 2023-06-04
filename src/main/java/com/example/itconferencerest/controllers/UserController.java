package com.example.itconferencerest.controllers;

import com.example.itconferencerest.validators.UniqueUsername;
import com.example.itconferencerest.models.Reservation;
import com.example.itconferencerest.models.Subject;
import com.example.itconferencerest.models.User;
import com.example.itconferencerest.service.ConferenceServiceImpl;
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
public class UserController {


    ConferenceServiceImpl service;


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

    @PutMapping({"/update/email/user/{id}"})
    public ResponseEntity<User> updateEmail(@PathVariable("id") Long id, @RequestParam("email") String email) {
        User user = service.updateEmail(id,email);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<User> saveUser(@RequestParam("login") String login, @RequestParam("email") String email) {
        User user = service.saveUser(login,email);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
