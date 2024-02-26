package com.reservationsystem.ReservationSystemApp.controller;

import com.reservationsystem.ReservationSystemApp.model.Lecture;
import com.reservationsystem.ReservationSystemApp.model.User;
import com.reservationsystem.ReservationSystemApp.dto.EventsFromUserResponse;
import com.reservationsystem.ReservationSystemApp.dto.LectureDto;
import com.reservationsystem.ReservationSystemApp.dto.LectureResponse;
import com.reservationsystem.ReservationSystemApp.dto.UserEventsResponse;
import com.reservationsystem.ReservationSystemApp.service.EmailService;
import com.reservationsystem.ReservationSystemApp.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/lecture")
@CrossOrigin(origins = "http://localhost:3000")
public class LectureController {
    private final EmailService emailService;
    public LectureController(EmailService emailService) { this.emailService = emailService; }
    @Autowired
    private LectureService lectureService;

    @PostMapping("")
    public Lecture createLecture(@RequestBody LectureDto lectureDto) {
        return lectureService.save(lectureDto);
    }

    @GetMapping("")
    public ResponseEntity<?> findAllLecturesWithPhotos(@AuthenticationPrincipal User user) {
        List<LectureResponse> lectures = lectureService.findAllLecturesWithPhotos();
        return ResponseEntity.ok(lectures);
    }

    @GetMapping("/userEvents")
    public ResponseEntity<?> findAllLecturesWithPhotosFromUsers(@AuthenticationPrincipal User user) {
        List<UserEventsResponse> lecturesFromUsers = lectureService.findAllLecturesWithPhotosFromUsers();
        return ResponseEntity.ok(lecturesFromUsers);
    }

    @GetMapping("/myEvents/{username}")
    public ResponseEntity<?> findAllLecturesWithPhotosFromUser(@PathVariable String username, @AuthenticationPrincipal User user) {
        List<EventsFromUserResponse> lecturesFromUser = lectureService.findAllLecturesWithPhotosFromUser(username);
        return ResponseEntity.ok(lecturesFromUser);
    }

    @GetMapping("{lectureId}")
    public ResponseEntity<?> getLecture(@PathVariable Long lectureId, @AuthenticationPrincipal User user) {
        LectureResponse lecture = lectureService.getLectureById(lectureId);
        return ResponseEntity.ok(lecture);
    }

    @PutMapping("{lectureId}")
    public ResponseEntity<?> updateLecture(@PathVariable Long lectureId, @RequestBody LectureDto lectureDto, @AuthenticationPrincipal User user) {
        String response = lectureService.update(lectureId, lectureDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{lectureId}")
    public ResponseEntity<?> deleteLecture(@PathVariable Long lectureId) {
        try {
            lectureService.delete(lectureId);
            return ResponseEntity.ok("Lecture deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/confirm/{lectureId}")
    public ResponseEntity<?> confirmUserEvent(@PathVariable Long lectureId, @RequestBody Map<String, Object> requestBody, @AuthenticationPrincipal User user) {
        String response = lectureService.confirmUserEvent(lectureId);
        String jsonString = "{\"response\": \" "+ response +" \"}";
        String userEmail = (String) requestBody.get("email");

        if (Objects.equals(response, "Successfully confirmed")) {
            String to = userEmail;
            String subject = "Your event has been posted!";
            String text = "Your event has been posted!";

            emailService.sendEmail(to, subject, text);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonString);
    }

    @DeleteMapping("/decline/{lectureId}")
    public ResponseEntity<?> deleteUserEvent(@PathVariable Long lectureId, @RequestBody Map<String, Object> requestBody, @AuthenticationPrincipal User user) {
        String userEmail = (String) requestBody.get("email");
        try {
            lectureService.delete(lectureId);
            String to = userEmail;
            String subject = "Your event has been declined";
            String text = "Your event has been declined by the Administrator";

            String jsonString = "{\"response\": \" "+ "Successfully declined" +" \"}";

            emailService.sendEmail(to, subject, text);
            return ResponseEntity.ok(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/cancel/{lectureId}")
    public ResponseEntity<?> cancelRequest(@PathVariable Long lectureId, @AuthenticationPrincipal User user) {
        String response = lectureService.cancelRequest(lectureId);

        String jsonString = "{\"response\": \" "+ response +" \"}";

        return ResponseEntity.ok(jsonString);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        List<String> categories = lectureService.getAllCategories();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{category}")
    public ResponseEntity<?> getLecturesByCategory(@PathVariable String category) {
        List<LectureResponse> lectures = lectureService.getLecturesByCategory(category);

        return ResponseEntity.ok(lectures);
    }

    @GetMapping("/location/{lectureId}")
    public ResponseEntity<?> getLocationForLecture(@PathVariable Long lectureId) {
        Object location = lectureService.getLocationForLecture(lectureId);

        return ResponseEntity.ok(location);
    }

}
