package com.example.bookingsitebackend.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.UUID;

@Service
public class GoogleCloudStorageService {
    private final Storage storage;
    public GoogleCloudStorageService() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("credentials.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
        this.storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
    public String uploadFile(MultipartFile file) throws IOException {
        String bucketName = "images_booking";
        String blobName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(),
                        Acl.Role.READER))).build();
        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);
    }
}
