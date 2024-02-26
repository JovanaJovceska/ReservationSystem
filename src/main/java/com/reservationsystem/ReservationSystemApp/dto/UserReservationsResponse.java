package com.reservationsystem.ReservationSystemApp.dto;

public class UserReservationsResponse {
    private Long lectureUserId;
    private Long lectureId;
    private String status;
    private int seats;
    private String lectureName;

    public UserReservationsResponse(Long lectureUserId, Long lectureId, String status, int seats, String lectureName) {
        this.lectureUserId = lectureUserId;
        this.lectureId = lectureId;
        this.status = status;
        this.seats = seats;
        this.lectureName = lectureName;
    }

    public Long getLectureUserId() {
        return lectureUserId;
    }

    public void setLectureUserId(Long lectureUserId) {
        this.lectureUserId = lectureUserId;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public void setLectureId(Long lectureId) {
        this.lectureId = lectureId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getLectureName() { return lectureName; }

    public void setLectureName(String lectureName) { this.lectureName = lectureName; }
}
