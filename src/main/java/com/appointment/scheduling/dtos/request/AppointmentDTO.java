package com.appointment.scheduling.dtos.request;

import com.appointment.scheduling.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {

    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String receptionPersonName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus appointmentStatus;
    private Long appointmentNumber;
}