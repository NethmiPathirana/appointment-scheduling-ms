package com.appointment.scheduling.repository;

import com.appointment.scheduling.dtos.request.DoctorAppointmentCountDTO;
import com.appointment.scheduling.dtos.response.AppointmentFrequencyDTO;
import com.appointment.scheduling.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Custom query method to find appointments by patient ID
    List<Appointment> findByPatientId(Long patientId);

    // Custom query method to find appointments by doctor ID
    List<Appointment> findByDoctorId(Long doctorId);

    // Custom query in AppointmentRepository to get the count of appointments for a specific doctor within a date range
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctorId = :doctorId " +
            "AND a.appointmentDate BETWEEN :startDate AND :endDate")
    Long countAppointmentsByDoctorIdAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Custom query to get the doctor appointment count by date
    @Query("SELECT new com.appointment.scheduling.dtos.request.DoctorAppointmentCountDTO(a.doctorName, COUNT(a)) " +
            "FROM Appointment a " +
            "WHERE a.appointmentDate = :date " +
            "GROUP BY a.doctorName")
    List<DoctorAppointmentCountDTO> countAppointmentsByDate(@Param("date") LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> getAppointmentsWithinDateRange(@Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);
}
