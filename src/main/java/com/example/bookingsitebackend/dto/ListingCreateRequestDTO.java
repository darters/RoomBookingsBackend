package com.example.bookingsitebackend.dto;

import com.example.bookingsitebackend.entity.Listing;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ListingCreateRequestDTO {
    private Listing listing;
    private List<MultipartFile> photoFiles;
}
