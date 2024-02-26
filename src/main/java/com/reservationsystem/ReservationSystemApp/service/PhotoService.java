package com.reservationsystem.ReservationSystemApp.service;

import com.reservationsystem.ReservationSystemApp.model.Lecture;
import com.reservationsystem.ReservationSystemApp.model.LecturePhoto;
import com.reservationsystem.ReservationSystemApp.exception.FileNotSupportedException;
import com.reservationsystem.ReservationSystemApp.dto.FileUploadResponse;
import com.reservationsystem.ReservationSystemApp.repository.LecturePhotoRepository;
import com.reservationsystem.ReservationSystemApp.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PhotoService {
    public PhotoService() throws IOException {
    }

    @Autowired
    private LecturePhotoRepository lecturePhotoRepository;

    @Autowired
    private LectureRepository lectureRepository;

    private final Path UPLOAD_PATH =
            Paths.get(new ClassPathResource("").getFile().getAbsolutePath()
                    + File.separator + "static"
                    + File.separator + "image");

    public FileUploadResponse uploadFile(MultipartFile file, Long lectureId) throws IOException {
        if (!Files.exists(UPLOAD_PATH)) {
            Files.createDirectories(UPLOAD_PATH);
        }

        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            throw new FileNotSupportedException("only .jpeg and .png images are " + "supported");
        }

        String timeStampedFileName = new SimpleDateFormat("ssmmHHddMMyyyy")
                .format(new Date()) + "_" + file.getOriginalFilename();

        Path filePath = UPLOAD_PATH.resolve(timeStampedFileName);
        Files.copy(file.getInputStream(), filePath);

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/").path(timeStampedFileName).toUriString();

        LecturePhoto lecturePhoto = new LecturePhoto(file.getOriginalFilename(),
                fileUri, file.getSize());

        this.lecturePhotoRepository.save(lecturePhoto);

        Lecture lecture = lectureRepository.getById(lectureId);
        lecture.setPhoto(lecturePhoto);
        lectureRepository.save(lecture);

        return new FileUploadResponse(Math.toIntExact(lecturePhoto.getId()),
                file.getOriginalFilename(), fileUri,
                file.getSize());
    }

    public FileUploadResponse updateFile(Long photoId, MultipartFile file) throws IOException{
        LecturePhoto lecturePhoto = lecturePhotoRepository.getById(photoId);
        String fileName = lecturePhoto.getFileUri().substring(28);
        File imageFile = new File(UPLOAD_PATH.toString(), fileName);

        imageFile.delete();

        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            throw new FileNotSupportedException("only .jpeg and .png images are " + "supported");
        }

        String timeStampedFileName = new SimpleDateFormat("ssmmHHddMMyyyy")
                .format(new Date()) + "_" + file.getOriginalFilename();

        Path filePath = UPLOAD_PATH.resolve(timeStampedFileName);
        Files.copy(file.getInputStream(), filePath);

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/").path(timeStampedFileName).toUriString();

        lecturePhoto.setFileName(file.getOriginalFilename());
        lecturePhoto.setFileUri(fileUri);
        lecturePhoto.setFileSize(file.getSize());

        lecturePhotoRepository.save(lecturePhoto);

        return new FileUploadResponse(Math.toIntExact(lecturePhoto.getId()),
                file.getOriginalFilename(), fileUri,
                file.getSize());
    }
}
