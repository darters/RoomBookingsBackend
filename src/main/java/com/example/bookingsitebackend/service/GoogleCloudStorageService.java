package com.example.bookingsitebackend.service;

import com.google.api.client.util.IOUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Collections;
import java.util.Iterator;
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
        MultipartFile compressedFile = compressFile(file);
        String bucketName = "images_booking";
        String blobName = UUID.randomUUID() + "-" + compressedFile.getOriginalFilename();

        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(compressedFile.getContentType())
                .setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(),
                        Acl.Role.READER))).build();
        storage.create(blobInfo, compressedFile.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);
    }


    private MultipartFile compressFile(MultipartFile inputFile) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputFile.getInputStream());
        if(inputFile == null) {
            throw new IOException("Invalid image file");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(inputImage)
                .size(800, 600)
                .outputQuality(0.5)
                .outputFormat("jpg")
                .toOutputStream(outputStream);

        byte[] compressedImageBytes = outputStream.toByteArray();
        InputStream compressedImageStream = new ByteArrayInputStream(compressedImageBytes);
        MultipartFile compressedFile = new MultipartFile() {
            @Override
            public String getName() {
                return inputFile.getName();
            }

            @Override
            public String getOriginalFilename() {
                return inputFile.getOriginalFilename();
            }

            @Override
            public String getContentType() {
                return "image/jpeg";
            }

            @Override
            public boolean isEmpty() {
                return compressedImageBytes.length == 0;
            }

            @Override
            public long getSize() {
                return compressedImageBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return compressedImageBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(compressedImageBytes);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                try (OutputStream out = new FileOutputStream(dest)) {
                    IOUtils.copy(new ByteArrayInputStream(compressedImageBytes), out);
                }
            }
        };
        return compressedFile;
    }
}
