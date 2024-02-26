package com.reservationsystem.ReservationSystemApp.repository;

import com.reservationsystem.ReservationSystemApp.model.LectureUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureUserRepository extends JpaRepository<LectureUser, Long> {
    @Query(value = "SELECT u.email, l.name, lu.seats, lu.id, l.id AS lId FROM lecture_user lu JOIN users u ON lu.user_id = u.id JOIN lecture l ON lu.lecture_id = l.id WHERE lu.user_id = u.id AND lu.confirmed = 0 AND lu.lecture_id = :lectureId", nativeQuery = true)
    List<Object[]> getAllReservationsRequests(@Param("lectureId") Long lectureId);

    @Query(value = "SELECT u.email, l.name, lu.seats, lu.id, l.id AS lId FROM lecture_user lu JOIN users u ON lu.user_id = u.id JOIN lecture l ON lu.lecture_id = l.id WHERE lu.user_id = u.id AND lu.confirmed = 1 AND lu.lecture_id = :lectureId", nativeQuery = true)
    List<Object[]> getAllReservations(@Param("lectureId") Long lectureId);

    @Query(value = "SELECT lu.id, lu.lecture_id, lu.confirmed, lu.seats FROM lecture_user lu INNER JOIN lecture l ON lu.lecture_id = l.id WHERE lu.user_id = :userId AND l.date_of_event > CURRENT_TIMESTAMP;", nativeQuery = true)
    List<Object[]> getAllUserReservations(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM lecture_user lu WHERE lecture_id = :lectureId", nativeQuery = true)
    List<Object[]> getAllReservationsByLecture(@Param("lectureId") Long lectureId);
}
