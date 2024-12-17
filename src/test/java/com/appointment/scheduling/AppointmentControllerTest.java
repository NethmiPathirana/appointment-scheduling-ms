package com.appointment.scheduling;

import com.appointment.scheduling.controller.AppointmentController;
import com.appointment.scheduling.dtos.request.AppointmentDTO;
import com.appointment.scheduling.entity.Appointment;
import com.appointment.scheduling.service.AppointmentService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddAppointment() throws Exception {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPatientId(1L);
        appointmentDTO.setDoctorId(2L);

        Appointment mockAppointment = new Appointment();
        mockAppointment.setId(1L);

        Mockito.when(appointmentService.addAppointment(any())).thenReturn(mockAppointment);

        mockMvc.perform(post("/health-sync/add-appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteAppointment() throws Exception {
        Mockito.when(appointmentService.deleteAppointment(1L)).thenReturn(true);

        mockMvc.perform(delete("/health-sync/delete-appointment/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment with ID 1 deleted successfully."));
    }

    @Test
    public void testDeleteAppointmentNotFound() throws Exception {
        Mockito.when(appointmentService.deleteAppointment(1L)).thenReturn(false);

        mockMvc.perform(delete("/health-sync/delete-appointment/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Appointment with ID 1 not found."));
    }

    @Test
    public void testEditAppointment() throws Exception {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPatientId(1L);
        appointmentDTO.setDoctorId(2L);

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setId(1L);

        Mockito.when(appointmentService.editAppointment(anyLong(), any())).thenReturn(updatedAppointment);

        mockMvc.perform(put("/health-sync/edit-appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetAppointmentById() throws Exception {
        Appointment appointment = new Appointment();
        appointment.setId(1L);

        Mockito.when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);

        mockMvc.perform(get("/health-sync/get-appointment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetAppointmentByIdNotFound() throws Exception {
        Mockito.when(appointmentService.getAppointmentById(1L)).thenReturn(null);

        mockMvc.perform(get("/health-sync/get-appointment/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Appointment with ID 1 not found."));
    }

    @Test
    public void testGenerateAppointmentFrequencyFileError() throws Exception {
        Mockito.when(appointmentService.generateAppointmentFrequencyFile(any()))
                .thenThrow(new RuntimeException("File generation failed"));

        mockMvc.perform(get("/health-sync/appointment-frequency")
                        .param("date", LocalDate.now().toString()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error generating the report: File generation failed"));
    }
}
