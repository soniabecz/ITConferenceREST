package com.example.itconferencerest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column
    private Long lectureID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Valid
    @JoinColumn(name = "userID")
    private User user;

}
