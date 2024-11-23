package com.appointment.scheduling.controller;

import com.appointment.scheduling.dtos.request.AppointmentDTO;
import com.appointment.scheduling.dtos.request.DoctorAppointmentCountDTO;
import com.appointment.scheduling.entity.Appointment;
import com.appointment.scheduling.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;

import java.util.List;

@RestController
@RequestMapping("/health-sync")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // POST: Add a new appointment
    @PostMapping("/add-appointment")
    public ResponseEntity<Appointment> addAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        Appointment createdAppointment = appointmentService.addAppointment(appointmentDTO);
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    // DELETE: Delete appointment by ID
    @DeleteMapping("/delete-appointment/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        boolean isDeleted = appointmentService.deleteAppointment(id);
        if (isDeleted) {
            return new ResponseEntity<>("Appointment with ID " + id + " deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Appointment with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    // PUT: Edit appointment by ID
    @PutMapping("/edit-appointment/{id}")
    public ResponseEntity<Object> editAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {
        Appointment updatedAppointment = appointmentService.editAppointment(id, appointmentDTO);
        if (updatedAppointment != null) {
            return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Appointment with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    // GET: Get appointment by ID
    @GetMapping("/get-appointment/{id}")
    public ResponseEntity<Object> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment != null) {
            return new ResponseEntity<>(appointment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Appointment with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    // GET: Get all appointments by patient ID
    @GetMapping("/get-appointments-by-patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        if (!appointments.isEmpty()) {
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET: Get all appointments by doctor ID
    @GetMapping("/get-appointments-by-doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
        if (!appointments.isEmpty()) {
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET: Get no of appointments for each doctor giving start and end date
    @GetMapping("/count-by-doctor")
    public ResponseEntity<Long> getAppointmentCountByDoctor(
            @RequestParam Long doctorId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        Long appointmentCount = appointmentService.getAppointmentCountByDoctorAndDateRange(doctorId, startDate, endDate);
        return ResponseEntity.ok(appointmentCount);
    }

    // Get List of doctors with no of appointments for a specific date
    @GetMapping("/doctor-appointment-counts")
    public ResponseEntity<List<DoctorAppointmentCountDTO>> getDoctorAppointmentCounts(
            @RequestParam("date") LocalDate date) {
        List<DoctorAppointmentCountDTO> appointmentCounts = appointmentService.getDoctorAppointmentCountsByDate(date);
        return ResponseEntity.ok(appointmentCounts);
    }

    // Get the total number of appointments for the last 7 days (including the given date)
    @GetMapping("/appointment-frequency")
    public ResponseEntity<?> getAppointmentFrequency(@RequestParam("date") LocalDate date) {
        try {
            // Call the service to generate the file
            File csvFile = appointmentService.generateAppointmentFrequencyFile(date);

            // Return the file as a downloadable response
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=appointment_frequency.csv")
                    .body(new InputStreamResource(new FileInputStream(csvFile)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating the report: " + e.getMessage());
        }
    }


}
