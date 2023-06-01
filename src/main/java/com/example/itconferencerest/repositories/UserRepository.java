package com.example.itconferencerest.repositories;

import com.example.itconferencerest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
