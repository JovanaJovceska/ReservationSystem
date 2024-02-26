package com.reservationsystem.ReservationSystemApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {
    private int id;
    private String fileName;
    private String fileUri;
    private long fileSize;
}
