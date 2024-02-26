package com.reservationsystem.ReservationSystemApp.repository;

import com.reservationsystem.ReservationSystemApp.model.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authorities, Long> {
}
