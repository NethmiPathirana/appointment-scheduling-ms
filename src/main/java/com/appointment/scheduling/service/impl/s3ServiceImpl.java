package com.appointment.scheduling.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.appointment.scheduling.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class s3ServiceImpl implements S3Service {
    private final AmazonS3 s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    s3ServiceImpl(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadFile(File file) {
        String fileName = file.getName();
        try {
            // Check if the file already exists in the bucket
            if (s3Client.doesObjectExist(bucketName, fileName)) {
                // Delete the existing file
                s3Client.deleteObject(bucketName, fileName);
            }

            // Upload the new file without setting an ACL
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));

            // Return the URL of the uploaded file
            return s3Client.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }
}
