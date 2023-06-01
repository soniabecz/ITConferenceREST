package com.example.itconferencerest.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lecture {

    private Long id;

    private List<Reservation> reservations;

    private Date startTime;

    private Date endTime;

    private Subject subject;

}
