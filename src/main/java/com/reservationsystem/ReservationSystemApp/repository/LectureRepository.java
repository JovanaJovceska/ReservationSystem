package com.reservationsystem.ReservationSystemApp.repository;

import com.reservationsystem.ReservationSystemApp.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Query(value = "SELECT * FROM lecture l INNER JOIN lecture_photo p ON p.id = l.photo_id WHERE l.confirmed = true AND l.date_of_event > CURRENT_TIMESTAMP", nativeQuery = true)
    List<Object[]> findAllLecturesWithPhotos();

    @Query(value = "SELECT l.*, p.*, u.email FROM lecture l INNER JOIN lecture_photo p ON p.id = l.photo_id INNER JOIN users u ON u.username = l.created_by WHERE l.confirmed = false AND l.date_of_event > CURRENT_TIMESTAMP", nativeQuery = true)
    List<Object[]> findAllLecturesWithPhotosFromUsers();

    @Query(value = "SELECT l.*, p.* FROM lecture l INNER JOIN lecture_photo p ON p.id = l.photo_id WHERE l.created_by = :username AND l.confirmed = false AND l.date_of_event > CURRENT_TIMESTAMP;", nativeQuery = true)
    List<Object[]> findAllLecturesWithPhotosFromUser(@Param("username") String username);

    @Query(value = "SELECT * FROM lecture l INNER JOIN lecture_photo p ON p.id = l.photo_id WHERE l.id = :lectureId", nativeQuery = true)
    Object findLectureWithPhotoById(@Param("lectureId") Long lectureId);

    @Query(value = "SELECT * FROM lecture WHERE lecture.name = :lectureName", nativeQuery = true)
    Object findByName(@Param("lectureName") String lectureName);

    @Query(value = "SELECT DISTINCT category FROM lecture;", nativeQuery = true)
    List<String> getAllCategories();

    @Query(value = "SELECT * FROM lecture l INNER JOIN lecture_photo p ON p.id = l.photo_id WHERE l.confirmed = true AND l.category = :category AND l.date_of_event > CURRENT_TIMESTAMP", nativeQuery = true)
    List<Object[]> getLecturesByCategory(@Param("category") String category);

    @Query(value = "SELECT latitude, longitude FROM lecture l WHERE l.id = :lectureId", nativeQuery = true)
    Object getLocationForLecture(@Param("lectureId") Long lectureId);
}