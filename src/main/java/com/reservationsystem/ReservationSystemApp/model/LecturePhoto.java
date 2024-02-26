package com.reservationsystem.ReservationSystemApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lecture_photo")
public class LecturePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUri;
    private long fileSize;

    @OneToOne(mappedBy = "photo", fetch = FetchType.LAZY)
    private Lecture lecture;

    public LecturePhoto() {}

    public LecturePhoto(String fileName, String fileUri, long fileSize) {
        this.fileName = fileName;
        this.fileUri = fileUri;
        this.fileSize = fileSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
