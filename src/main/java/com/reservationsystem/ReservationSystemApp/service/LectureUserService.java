package com.reservationsystem.ReservationSystemApp.service;

import com.reservationsystem.ReservationSystemApp.model.Lecture;
import com.reservationsystem.ReservationSystemApp.model.LectureUser;
import com.reservationsystem.ReservationSystemApp.model.User;
import com.reservationsystem.ReservationSystemApp.dto.LectureUserDto;
import com.reservationsystem.ReservationSystemApp.dto.LectureUserResponse;
import com.reservationsystem.ReservationSystemApp.dto.UserReservationsResponse;
import com.reservationsystem.ReservationSystemApp.enums.LectureStatusEnum;
import com.reservationsystem.ReservationSystemApp.repository.LectureRepository;
import com.reservationsystem.ReservationSystemApp.repository.LectureUserRepository;
import com.reservationsystem.ReservationSystemApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LectureUserService {
    @Autowired
    private LectureUserRepository lectureUserRepo;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserRepository userRepository;

    public String createReservationRequest(LectureUserDto lectureUserDto) {
        LectureUser newLectureUser = new LectureUser();

        Optional<User> optionalUser = userRepository.findByUsername(lectureUserDto.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Lecture lecture = lectureRepository.getById(lectureUserDto.getLecture_id());

            newLectureUser.setUser(user);
            newLectureUser.setLecture(lecture);
            newLectureUser.setSeats(lectureUserDto.getSeats());

            lectureUserRepo.save(newLectureUser);

            return "Successfully sent a reservation request";
        } else {
            return "Something went wrong";
        }
    }

    public List<LectureUserResponse> getAllReservationRequests(Long lectureId) {
        List<Object[]> result = lectureUserRepo.getAllReservationsRequests(lectureId);
        List<LectureUserResponse> responses = new ArrayList<>();
        for (Object[] row : result) {
            String email = (String) row[0];
            String lecture_name = (String) row[1];
            int seats = (int) row[2];
            Long id = (Long) row[3];
            Long lId = (Long) row[4];

            responses.add(new LectureUserResponse(id, lId, email, lecture_name, seats));
        }

        return responses;
    }

    public List<LectureUserResponse> getAllReservations(Long lectureId) {
        List<Object[]> result = lectureUserRepo.getAllReservations(lectureId);
        List<LectureUserResponse> responses = new ArrayList<>();
        for (Object[] row : result) {
            String email = (String) row[0];
            String lecture_name = (String) row[1];
            int seats = (int) row[2];
            Long id = (Long) row[3];
            Long lId = (Long) row[4];

            responses.add(new LectureUserResponse(id, lId, email, lecture_name, seats));
        }

        return responses;
    }

    public String confirmReservationRequest(long lectureUserId, Long lectureId) {
        LectureUser lectureUser = lectureUserRepo.getById(lectureUserId);

        Lecture lecture = lectureRepository.getById(lectureId);

        if (lecture.getDateOfEvent().isBefore(LocalDateTime.now())) {
            deleteReservationRequest(lectureUserId);
            return "Event is finished...";
        }

        if (lecture.getAvailableSeats() >= lectureUser.getSeats()) {
            lectureUser.setConfirmed(true);
            lecture.setAvailableSeats(lecture.getAvailableSeats() - lectureUser.getSeats());
            lectureUserRepo.save(lectureUser);
            lectureRepository.save(lecture);
            if (lecture.getAvailableSeats() == 0) {
                lecture.setStatus(LectureStatusEnum.BOOKED.getStatus());
                lectureRepository.save(lecture);
            }
            return "Successful reservation";
        } else {
            if (lecture.getAvailableSeats() == 0) {
                lecture.setStatus(LectureStatusEnum.BOOKED.getStatus());
                lectureRepository.save(lecture);
            }
            deleteReservationRequest(lectureUserId);
            return "There are not available seats. Delete the Reservation request and send email";
        }
    }

    public void deleteReservationRequest(long lectureUserId) {
        lectureUserRepo.deleteById(lectureUserId);
    }

    public List<UserReservationsResponse> getAllUserReservationRequests(Long userId) {
        List<Object[]> result = lectureUserRepo.getAllUserReservations(userId);
        List<UserReservationsResponse> responses = new ArrayList<>();
        for (Object[] row : result) {
            Long lectureUserId = (Long) row[0];
            Long lectureId = (Long) row[1];
            boolean confirmed = (boolean) row[2];
            String status = "";
            if (confirmed) {
                status = "Approved";
            } else {
                status = "Pending Approval";
            }
            int seats = (int) row[3];
            Lecture lecture = lectureRepository.getById(lectureId);
            String lectureName = lecture.getName();

            responses.add(new UserReservationsResponse(lectureUserId, lectureId, status, seats, lectureName));
        }

        return responses;
    }

    public String cancelReservation(Long lectureUserId, Long lectureId, int seats) {
        LectureUser lectureUser = lectureUserRepo.getById(lectureUserId);
        if (lectureUser.getConfirmed()) {
            Lecture lecture = lectureRepository.getById(lectureId);
            lecture.setAvailableSeats(lecture.getAvailableSeats() + seats);
            lectureRepository.save(lecture);
        }

        deleteReservationRequest(lectureUserId);
        return "You successfully canceled your reservation";
    }
}
