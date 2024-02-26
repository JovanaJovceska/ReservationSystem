package com.reservationsystem.ReservationSystemApp.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LectureStatusEnum {
    AVAILABLE("Available", 1),
    BOOKED("Booked", 2),
    FINISHED("Finished", 3);

    private String status;
    private Integer step;

    LectureStatusEnum(String status, Integer step) {
        this.status = status;
        this.step = step;
    }

    public String getStatus() {return status;}
    public Integer getStep() {return step;}
}
