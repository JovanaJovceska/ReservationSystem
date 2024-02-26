package com.reservationsystem.ReservationSystemApp.service;

import com.reservationsystem.ReservationSystemApp.model.Lecture;
import com.reservationsystem.ReservationSystemApp.model.LecturePhoto;
import com.reservationsystem.ReservationSystemApp.dto.EventsFromUserResponse;
import com.reservationsystem.ReservationSystemApp.dto.LectureDto;
import com.reservationsystem.ReservationSystemApp.dto.LectureResponse;
import com.reservationsystem.ReservationSystemApp.dto.UserEventsResponse;
import com.reservationsystem.ReservationSystemApp.enums.LectureStatusEnum;
import com.reservationsystem.ReservationSystemApp.repository.LecturePhotoRepository;
import com.reservationsystem.ReservationSystemApp.repository.LectureRepository;
import com.reservationsystem.ReservationSystemApp.repository.LectureUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class LectureService {

    public LectureService() throws IOException {
    }

    @Autowired
    private LectureRepository lectureRepo;

    @Autowired
    private LectureUserRepository lectureUserRepo;

    @Autowired
    private LecturePhotoRepository lecturePhotoRepository;

    public Lecture save(LectureDto lectureDto) {
        Lecture lecture = new Lecture();

        lecture.setName(lectureDto.getName());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setAvailableSeats(lectureDto.getAvailableSeats());
        lecture.setDateOfEvent(lectureDto.getDateOfEvent());
        lecture.setCreatedBy(lectureDto.getCreatedBy());
        lecture.setCategory(lectureDto.getCategory());
        lecture.setLatitude(lectureDto.getLatitude());
        lecture.setLongitude(lectureDto.getLongitude());
        lecture.setEventType(lectureDto.getEventType());
        lecture.setConfirmed(Objects.equals(lectureDto.getCreatedBy(), "admin"));

        Date now = new Date();

        if (lectureDto.getDateOfEvent().isBefore(LocalDateTime.now())) {
            lecture.setStatus(LectureStatusEnum.FINISHED.getStatus());
        } else {
            lecture.setStatus(LectureStatusEnum.AVAILABLE.getStatus());
        }

        return lectureRepo.save(lecture);
    }

    public List<LectureResponse> findAllLecturesWithPhotos() {
        List<Object[]> result = lectureRepo.findAllLecturesWithPhotos();
        List<LectureResponse> responses = new ArrayList<>();
        for (Object[] row : result) {
            Long lectureId = (Long) row[0];
            int availableSeats = (int) row[1];
            String description = (String) row[6];

            Timestamp timestamp = (Timestamp) row[5];
            LocalDateTime dateOfEvent = timestamp.toLocalDateTime();

            String name = (String) row[10];
            String status = (String) row[11];
            String createdBy = (String) row[4];
            String category = (String) row[2];
            BigDecimal latitude = (BigDecimal) row[8];
            BigDecimal longitude = (BigDecimal) row[9];
            String eventType = (String) row[7];
            Long photoId = (Long) row[13];
            String fileName = (String) row[14];
            Long fileSize = (Long) row[15];
            String fileUri = (String) row[16];

            responses.add(new LectureResponse(lectureId, name, description, status, availableSeats, dateOfEvent, createdBy, category, latitude, longitude, eventType, photoId, fileName, fileUri, fileSize));
        }

        return responses;
    }

    public List<UserEventsResponse> findAllLecturesWithPhotosFromUsers() {
        List<Object[]> result = lectureRepo.findAllLecturesWithPhotosFromUsers();
        List<UserEventsResponse> responses = new ArrayList<>();
        for (Object[] row : result) {
            Long lectureId = (Long) row[0];
            int availableSeats = (int) row[1];
            String description = (String) row[6];

            Timestamp timestamp = (Timestamp) row[5];
            LocalDateTime dateOfEvent = timestamp.toLocalDateTime();

            String name = (String) row[10];
            String status = (String) row[11];
            String createdBy = (String) row[4];
            String category = (String) row[2];
            BigDecimal latitude = (BigDecimal) row[8];
            BigDecimal longitude = (BigDecimal) row[9];
            String eventType = (String) row[7];
            Long photoId = (Long) row[13];
            String fileName = (String) row[14];
            Long fileSize = (Long) row[15];
            String fileUri = (String) row[16];
            String email = (String) row[17];

            responses.add(new UserEventsResponse(lectureId, name, description, status, availableSeats, dateOfEvent, createdBy, category, latitude, longitude, eventType, email, photoId, fileName, fileUri, fileSize));
        }

        return responses;
    }

    public LectureResponse getLectureById(Long lectureId) {
        Object result = lectureRepo.findLectureWithPhotoById(lectureId);

        if (result != null) {
            Object[] data = (Object[]) result;
            Timestamp timestamp = (Timestamp) data[5];
            LocalDateTime dateOfEvent = timestamp.toLocalDateTime();

            return new LectureResponse((Long) data[0], (String) data[7], (String) data[6], (String) data[8], (int) data[1], dateOfEvent, (String) data[4], (String) data[2], (BigDecimal) data[10], (BigDecimal) data[11], (String) data[12], (Long) data[13], (String) data[14], (String) data[16], (Long) data[15]);

        } else {
            return null;
        }
    }

    public String update(Long lectureId, LectureDto lectureDto) {
        Lecture lecture = lectureRepo.getById(lectureId);

        lecture.setName(lectureDto.getName());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setAvailableSeats(lectureDto.getAvailableSeats());
        lecture.setDateOfEvent(lectureDto.getDateOfEvent());
        lecture.setCategory(lectureDto.getCategory());
        lecture.setLatitude(lectureDto.getLatitude());
        lecture.setLongitude(lectureDto.getLongitude());
        lecture.setEventType(lectureDto.getEventType());

        if (lecture.getAvailableSeats() == 0) {
            lecture.setStatus(LectureStatusEnum.BOOKED.getStatus());
        } else {
            lecture.setStatus(LectureStatusEnum.AVAILABLE.getStatus());
        }

        lectureRepo.save(lecture);
        return "Successfully updated";
    }

    private final Path UPLOAD_PATH =
            Paths.get(new ClassPathResource("").getFile().getAbsolutePath()
                    + File.separator + "static"
                    + File.separator + "image");

    public void delete(Long lectureId) {
        Lecture lecture = lectureRepo.getById(lectureId);

        Long photoId = lecture.getPhoto().getId();
        LecturePhoto lecturePhoto = lecturePhotoRepository.getById(photoId);
        String fileName = lecturePhoto.getFileUri().substring(28);
        File imageFile = new File(UPLOAD_PATH.toString(), fileName);
        imageFile.delete();

        List<Object[]> reservations = lectureUserRepo.getAllReservationsByLecture(lectureId);
        for (Object[] row : reservations) {
            lectureUserRepo.deleteById((Long) row[0]);
        }

        lectureRepo.deleteById(lectureId);
    }

    public String confirmUserEvent(Long lectureId) {
        Lecture lecture = lectureRepo.getById(lectureId);
        lecture.setConfirmed(true);
        lectureRepo.save(lecture);
        return "Successfully confirmed";
    }

    public List<EventsFromUserResponse> findAllLecturesWithPhotosFromUser(String username) {
        List<Object[]> result = lectureRepo.findAllLecturesWithPhotosFromUser(username);
        List<EventsFromUserResponse> responses = new ArrayList<>();
        for (Object[] row : result) {
            Long lectureId = (Long) row[0];
            int availableSeats = (int) row[1];
            String description = (String) row[6];

            Timestamp timestamp = (Timestamp) row[5];
            LocalDateTime dateOfEvent = timestamp.toLocalDateTime();

            String name = (String) row[10];
            String status = (String) row[11];
            String category = (String) row[2];
            BigDecimal latitude = (BigDecimal) row[8];
            BigDecimal longitude = (BigDecimal) row[9];
            String eventType = (String) row[7];
            Long photoId = (Long) row[13];
            String fileName = (String) row[14];
            Long fileSize = (Long) row[15];
            String fileUri = (String) row[16];

            responses.add(new EventsFromUserResponse(lectureId, name, description, status, availableSeats, dateOfEvent, category, latitude, longitude, eventType, photoId, fileName, fileUri, fileSize));
        }

        return responses;
    }

    public String cancelRequest(Long lectureId) {
        delete(lectureId);
        return "You successfully canceled your event request";
    }

    public List<String> getAllCategories() {
        return lectureRepo.getAllCategories();
    }

    public List<LectureResponse> getLecturesByCategory(String category) {
        List<Object[]> result = lectureRepo.getLecturesByCategory(category);
        List<LectureResponse> responses = new ArrayList<>();

        for (Object[] row : result) {
            Long lectureId = (Long) row[0];
            int availableSeats = (int) row[1];
            String description = (String) row[6];

            Timestamp timestamp = (Timestamp) row[5];
            LocalDateTime dateOfEvent = timestamp.toLocalDateTime();

            String name = (String) row[7];
            String status = (String) row[8];
            String createdBy = (String) row[4];
            String categoryName = (String) row[2];
            BigDecimal latitude = (BigDecimal) row[10];
            BigDecimal longitude = (BigDecimal) row[11];
            String eventType = (String) row[12];
            Long photoId = (Long) row[13];
            String fileName = (String) row[14];
            Long fileSize = (Long) row[15];
            String fileUri = (String) row[16];

            responses.add(new LectureResponse(lectureId, name, description, status, availableSeats, dateOfEvent, createdBy, categoryName, latitude, longitude, eventType, photoId, fileName, fileUri, fileSize));
        }

        return responses;
    }

    public Object getLocationForLecture (Long lectureId){

        return lectureRepo.getLocationForLecture(lectureId);
    }
}
