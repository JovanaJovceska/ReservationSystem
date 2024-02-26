package com.reservationsystem.ReservationSystemApp.repository;

import com.reservationsystem.ReservationSystemApp.model.LecturePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturePhotoRepository extends JpaRepository<LecturePhoto, Long> {
}
