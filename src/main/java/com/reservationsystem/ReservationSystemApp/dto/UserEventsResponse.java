package com.reservationsystem.ReservationSystemApp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserEventsResponse {
    private Long lectureId;
    private String name;
    private String description;
    private String status;
    private int availableSeats;
    private LocalDateTime dateOfEvent;
    private String createdBy;
    private String category;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String eventType;
    private String email;

    private Long photoId;
    private String fileName;
    private String fileUri;
    private long fileSize;

    public UserEventsResponse(Long lectureId, String name, String description, String status, int availableSeats, LocalDateTime dateOfEvent, String createdBy, String category, BigDecimal latitude, BigDecimal longitude, String eventType, String email, Long photoId, String fileName, String fileUri, long fileSize) {
        this.lectureId = lectureId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.availableSeats = availableSeats;
        this.dateOfEvent = dateOfEvent;
        this.createdBy = createdBy;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventType = eventType;
        this.email = email;
        this.photoId = photoId;
        this.fileName = fileName;
        this.fileUri = fileUri;
        this.fileSize = fileSize;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public void setLectureId(Long lectureId) {
        this.lectureId = lectureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public LocalDateTime getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(LocalDateTime dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
