package com.reservationsystem.ReservationSystemApp.controller;

import com.reservationsystem.ReservationSystemApp.model.User;
import com.reservationsystem.ReservationSystemApp.dto.LectureUserDto;
import com.reservationsystem.ReservationSystemApp.dto.LectureUserResponse;
import com.reservationsystem.ReservationSystemApp.dto.UserReservationsResponse;
import com.reservationsystem.ReservationSystemApp.service.EmailService;
import com.reservationsystem.ReservationSystemApp.service.LectureUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/api/reserve")
@CrossOrigin(origins = "http://localhost:3000")
public class LectureUserController {
    private final EmailService emailService;

    public LectureUserController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    private LectureUserService lectureUserService;

    @PostMapping("")
    public ResponseEntity<String> createReservationRequest(@RequestBody LectureUserDto lectureUserDto, @AuthenticationPrincipal User user) {
        String response = lectureUserService.createReservationRequest(lectureUserDto);
        String jsonString = "{\"response\": \" "+ response +" \"}";

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonString);
    }

    @PostMapping("{lectureUserId}")
    public ResponseEntity<?> confirmReservationRequest(@PathVariable Long lectureUserId, @RequestBody Map<String, Object> requestBody, @AuthenticationPrincipal User user) {
        Long lectureId = ((Number) requestBody.get("lectureId")).longValue();
        String userEmail = (String) requestBody.get("email");

        String response = lectureUserService.confirmReservationRequest(lectureUserId, lectureId);
        String jsonString = "{\"response\": \" "+ response +" \"}";

        if (Objects.equals(response, "Successful reservation")) {
            String to = userEmail;
            String subject = "Reservation successful!";
            String text = "Your reservation is successful!";

            emailService.sendEmail(to, subject, text);
        } else {
            String to = userEmail;
            String subject = "Reservation denied";
            String text = "We're sorry, something went wrong...";

            emailService.sendEmail(to, subject, text);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonString);
    }

    @GetMapping("/requests/{lectureId}")
    public ResponseEntity<?> getAllReservationRequests(@PathVariable Long lectureId, @AuthenticationPrincipal User user) {
        List<LectureUserResponse> reservationRequests = lectureUserService.getAllReservationRequests(lectureId);
        return ResponseEntity.ok(reservationRequests);
    }

    @GetMapping("/reservations/{lectureId}")
    public ResponseEntity<?> getAllReservations(@PathVariable Long lectureId, @AuthenticationPrincipal User user) {
        List<LectureUserResponse> reservations = lectureUserService.getAllReservations(lectureId);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("{lectureUserId}")
    public ResponseEntity<?> deleteReservationRequest(@PathVariable Long lectureUserId, @RequestBody Map<String, Object> requestBody, @AuthenticationPrincipal User user) {
        String userEmail = (String) requestBody.get("email");
        try {
            lectureUserService.deleteReservationRequest(lectureUserId);

            String to = userEmail;
            String subject = "Reservation denied";
            String text = "Your reservation has been deleted by the Administrator... ";
            emailService.sendEmail(to, subject, text);

            String response = "Successfully deleted reservation request.";
            return ResponseEntity.ok("{\"response\": \" "+ response +" \"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getUserReservationRequests(@PathVariable Long userId, @AuthenticationPrincipal User user) {
        List<UserReservationsResponse> userReservationRequests = lectureUserService.getAllUserReservationRequests(userId);
        return ResponseEntity.ok(userReservationRequests);
    }

    @PostMapping("/cancel/{lectureUserId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long lectureUserId, @RequestBody Map<String, Object> requestBody, @AuthenticationPrincipal User user) {
        Long lectureId = ((Number) requestBody.get("lectureId")).longValue();
        int seats = (int) requestBody.get("seats");
        String response = lectureUserService.cancelReservation(lectureUserId, lectureId, seats);
        String jsonString = "{\"response\": \" "+ response +" \"}";

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonString);
    }

}
