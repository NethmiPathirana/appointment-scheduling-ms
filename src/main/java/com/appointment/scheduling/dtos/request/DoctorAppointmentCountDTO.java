package com.appointment.scheduling.dtos.request;

import lombok.Data;

@Data
public class DoctorAppointmentCountDTO {
    String doctorName;
    Long appointmentCount;

    public DoctorAppointmentCountDTO(String doctorName, Long appointmentCount) {
        this.doctorName = doctorName;
        this.appointmentCount = appointmentCount;
    }
}
