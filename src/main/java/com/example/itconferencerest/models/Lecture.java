package com.example.itconferencerest.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class Lecture {

    private Long id;

    private List<Reservation> reservations;

    private Date startTime;

    private Date endTime;

    private Subject subject;

}
