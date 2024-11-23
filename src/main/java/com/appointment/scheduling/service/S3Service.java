package com.appointment.scheduling.service;

import java.io.File;

public interface S3Service {
    String uploadFile(File file);
}
