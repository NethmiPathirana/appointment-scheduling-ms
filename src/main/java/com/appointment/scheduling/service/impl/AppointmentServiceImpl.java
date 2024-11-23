package com.appointment.scheduling.service.impl;

import com.appointment.scheduling.dtos.request.AppointmentDTO;
import com.appointment.scheduling.dtos.request.DoctorAppointmentCountDTO;
import com.appointment.scheduling.entity.Appointment;
import com.appointment.scheduling.repository.AppointmentRepository;
import com.appointment.scheduling.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment addAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setDoctorId(appointmentDTO.getDoctorId());
        appointment.setDoctorName(appointmentDTO.getDoctorName());
        appointment.setPatientId(appointmentDTO.getPatientId());
        appointment.setPatientName(appointmentDTO.getPatientName());
        appointment.setReceptionPersonName(appointmentDTO.getReceptionPersonName());
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
        appointment.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
        appointment.setAppointmentNumber(appointmentDTO.getAppointmentNumber());
        return appointmentRepository.save(appointment);
    }

    // Delete appointment by ID
    public boolean deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // Edit appointment by ID
    public Appointment editAppointment(Long id, AppointmentDTO appointmentDTO) {
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
        if (existingAppointment.isPresent()) {
            Appointment appointment = existingAppointment.get();
            appointment.setDoctorId(appointmentDTO.getDoctorId());
            appointment.setDoctorName(appointmentDTO.getDoctorName());
            appointment.setPatientId(appointmentDTO.getPatientId());
            appointment.setPatientName(appointmentDTO.getPatientName());
            appointment.setReceptionPersonName(appointmentDTO.getReceptionPersonName());
            appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
            appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
            appointment.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
            appointment.setAppointmentNumber(appointmentDTO.getAppointmentNumber());
            return appointmentRepository.save(appointment);
        } else {
            return null;
        }
    }

    // Get appointment by ID
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    // Get all appointments by patient ID
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // Get all appointments by doctor ID
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Override
    public Long getAppointmentCountByDoctorAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate) {
        return appointmentRepository.countAppointmentsByDoctorIdAndDateRange(doctorId, startDate, endDate);

    }

    @Override
    public List<DoctorAppointmentCountDTO> getDoctorAppointmentCountsByDate(LocalDate date) {
        return appointmentRepository.countAppointmentsByDate(date);
    }

    @Override
    public File generateAppointmentFrequencyFile(LocalDate date) throws IOException {
        // Calculate the start and end date for the last 7 days
        LocalDate startDate = date.minusDays(7);

        List<Appointment> appointments = appointmentRepository.getAppointmentsWithinDateRange(startDate, date);

        if (appointments.isEmpty()) {
            logger.warn("No appointments found in the date range from {} to {}", startDate, date);
        }

        // Define the output CSV file
        File csvFile = new File("appointments_frequency_file_test.csv");

        // Use CSVFormat.Builder to handle deprecated methods
        try (FileWriter writer = new FileWriter(csvFile);
             CSVPrinter csvPrinter = new CSVPrinter(writer,
                     CSVFormat.DEFAULT
                             .builder()
                             .setHeader("Appointment Number", "Doctor ID", "Doctor Name",
                                     "Appointment Date", "Appointment Status", "ID")
                             .setQuoteMode(org.apache.commons.csv.QuoteMode.MINIMAL)
                             .build())) {

            // Date and time formatters
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Loop through the appointments and write to CSV
            for (Appointment appointment : appointments) {
                csvPrinter.printRecord(
                        appointment.getAppointmentNumber(),
                        appointment.getDoctorId(),
                        appointment.getDoctorName(),
                        appointment.getAppointmentDate().format(dateFormatter),
                        appointment.getAppointmentStatus(),
                        appointment.getId()
                );
            }
        }

        // Return the CSV file
        return csvFile;
    }
}
