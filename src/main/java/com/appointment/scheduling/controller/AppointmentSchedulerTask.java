package com.appointment.scheduling.controller;

import com.appointment.scheduling.service.AppointmentService;
import com.appointment.scheduling.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class AppointmentSchedulerTask {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentSchedulerTask.class);

    private final AppointmentService appointmentService;
    private final S3Service s3Service;

    public AppointmentSchedulerTask(AppointmentService appointmentService, S3Service s3Service) {
        this.appointmentService = appointmentService;
        this.s3Service = s3Service;
    }

    @Scheduled(cron = "*/30 * * * * *") // Cron expression runs every 30 seconds
    public void generateAppointmentFrequencyReport() {
        LocalDate currentDate = LocalDate.now();
        try {
            // Generate the report file
            File reportFile = appointmentService.generateAppointmentFrequencyFile(currentDate);
            logger.info("Generated report file: {}", reportFile.getAbsolutePath());

            // Upload the generated file to S3
            String s3Url = s3Service.uploadFile(reportFile);
            logger.info("Uploaded report to S3: {}", s3Url);
        } catch (IOException e) {
            logger.error("Error generating appointment frequency report: {}", e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error uploading report to S3: {}", e.getMessage());
        }
    }

}
