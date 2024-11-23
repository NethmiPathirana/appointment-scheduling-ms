package com.appointment.scheduling.service;

import com.appointment.scheduling.dtos.request.AppointmentDTO;
import com.appointment.scheduling.dtos.request.DoctorAppointmentCountDTO;
import com.appointment.scheduling.entity.Appointment;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    Appointment addAppointment(AppointmentDTO appointmentDTO);

    boolean deleteAppointment(Long id);

    Appointment editAppointment(Long id, AppointmentDTO appointmentDTO);

    Appointment getAppointmentById(Long id);

    List<Appointment> getAppointmentsByPatientId(Long patientId);

    List<Appointment> getAppointmentsByDoctorId(Long doctorId);

    Long getAppointmentCountByDoctorAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate);

    List<DoctorAppointmentCountDTO> getDoctorAppointmentCountsByDate(LocalDate date);

    File generateAppointmentFrequencyFile(LocalDate date) throws IOException;
}
