package com.example.itconferencerest.repositories;

import com.example.itconferencerest.models.Reservation;
import com.example.itconferencerest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByLogin(String login);

}
