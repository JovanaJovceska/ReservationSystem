package com.reservationsystem.ReservationSystemApp.dto;

public class LectureUserResponse {
    private Long lectureUserId;
    private Long lectureId;
    private String email;
    private String lecture_name;
    private int seats;

    public LectureUserResponse(Long lectureUserId, Long lectureId, String email, String lecture_name, int seats) {
        this.lectureUserId = lectureUserId;
        this.lectureId = lectureId;
        this.email = email;
        this.lecture_name = lecture_name;
        this.seats = seats;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLecture_name() {
        return lecture_name;
    }

    public void setLecture_name(String lecture_name) {
        this.lecture_name = lecture_name;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
