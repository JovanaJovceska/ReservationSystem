package com.reservationsystem.ReservationSystemApp.controller;

import com.reservationsystem.ReservationSystemApp.exception.FileNotSupportedException;
import com.reservationsystem.ReservationSystemApp.dto.FileUploadResponse;
import com.reservationsystem.ReservationSystemApp.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/photo")
@CrossOrigin(origins = "http://localhost:3000")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @PostMapping
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("lectureId") Long lectureId) {
        try {
            FileUploadResponse fileUploadResponse = photoService.uploadFile(file, lectureId);
            return new ResponseEntity<>(fileUploadResponse, HttpStatus.OK);
        }
        catch (FileNotSupportedException e) {
            return new ResponseEntity<>("File not supported: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("{photoId}")
    public ResponseEntity<?> updateFile(@PathVariable Long photoId, @RequestParam("file") MultipartFile file) {
        try {
            FileUploadResponse fileUploadResponse = photoService.updateFile(photoId, file);
            return new ResponseEntity<>(fileUploadResponse, HttpStatus.OK);
        } catch (FileNotSupportedException e) {
            return new ResponseEntity<>("File not supported: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
