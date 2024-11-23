package com.appointment.scheduling.entity;

import com.appointment.scheduling.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_number", nullable = false)
    private Long appointmentNumber;  // Appointment number

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;  // Doctor ID

    @Column(name = "doctor_name", nullable = false)
    private String doctorName;  // Doctor's name

    @Column(name = "patient_id", nullable = false)
    private Long patientId;  // Patient ID

    @Column(name = "patient_name", nullable = false)
    private String patientName; // Patient's name

    @Column(name = "reception_person_name", nullable = false)
    private String receptionPersonName; // Reception person's name who created the appointment

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;  // Appointment date

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;  // Appointment time

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status", nullable = false)
    private AppointmentStatus appointmentStatus; // Appointment status
}
