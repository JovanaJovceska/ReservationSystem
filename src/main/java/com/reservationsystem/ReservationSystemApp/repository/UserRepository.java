package com.reservationsystem.ReservationSystemApp.repository;

import com.reservationsystem.ReservationSystemApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u.id FROM users u WHERE u.username = :username", nativeQuery = true)
    Long getUserId(@Param("username") String username);
}
