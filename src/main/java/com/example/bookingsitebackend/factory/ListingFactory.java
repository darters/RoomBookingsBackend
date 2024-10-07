package com.example.bookingsitebackend.factory;

import com.example.bookingsitebackend.entity.Listing;
import com.example.bookingsitebackend.service.GoogleCloudStorageService;
import com.example.bookingsitebackend.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListingFactory {
    @Autowired
    GoogleCloudStorageService googleCloudStorageService;
    public static Listing createListing(String address, List<String> photoUrls) {
        Listing listing = new Listing();
        listing.setAddress(address);
        listing.setPhotosUrl(photoUrls);
        String[] addressWord = listing.getAddress().replace(",", "").split(" ");
        String city = addressWord[addressWord.length - 2];
        listing.setCity(city);
        return listing;
    }

}
