package com.example.bookingsitebackend.factory;

import com.example.bookingsitebackend.entity.Listing;
import java.util.List;

public class ListingFactory {
    public static Listing createListing(Listing listing, String city, List<String> photoUrls) {
        listing.setCity(city);
        listing.setPhotosUrl(photoUrls);
        return listing;
    }
}

