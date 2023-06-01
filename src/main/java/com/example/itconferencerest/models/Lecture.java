package com.example.itconferencerest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lectures")
    private List<Reservation> reservations;

    @Column
    private Date startTime;

    @Column
    private Date endTime;

    private Subject subject;

}
