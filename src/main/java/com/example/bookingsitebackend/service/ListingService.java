package com.example.bookingsitebackend.service;

import com.example.bookingsitebackend.dto.ListingCreateRequestDTO;
import com.example.bookingsitebackend.entity.Listing;
import com.example.bookingsitebackend.factory.ListingFactory;
import com.example.bookingsitebackend.repository.ListingRepository;
import com.example.bookingsitebackend.specification.ListingSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ListingService {
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;
    public List<Listing> getListingWithFilters(String city, Long minPrice, Long maxPrice, Long rooms) {
        Specification<Listing> specification = Specification.where(ListingSpecification.getByCity(city));
        if (minPrice != null && maxPrice != null) {
            if (minPrice >= maxPrice) {
                throw new IllegalArgumentException("Invalid price range. minPrice cannot be greater than maxPrice");
            }
            specification = specification.and(ListingSpecification.priceRange(minPrice, maxPrice));
        }
        if (rooms != null && rooms >= 0) {
            specification = specification.and(ListingSpecification.countOfRooms(rooms));
        }
        return listingRepository.findAll(specification);
    }

    public ResponseEntity<String> deleteListing(String path) {
        String rangeRegex = "(\\d+)(?:-(\\d+))?";
        Pattern pattern = Pattern.compile(rangeRegex);
        Matcher matcher = pattern.matcher(path);
        if(matcher.matches()) {
            int startRoomId = Integer.parseInt(matcher.group(1));
            String endRoomId = matcher.group(2);
            if(endRoomId != null) {
                listingRepository.deleteListingsByRange(startRoomId, Integer.parseInt(endRoomId));
                return new ResponseEntity<>("Listings were deleted successfully", HttpStatus.OK);
            }
            listingRepository.deleteById(startRoomId);
        }
        return new ResponseEntity<>("Something was wrong", HttpStatus.BAD_REQUEST);
    }
    public Listing createListing(ListingCreateRequestDTO listingCreateRequestDTO) throws IOException {
        try {
            Listing listing = ListingFactory.createListing(listingCreateRequestDTO.getListing().getAddress(), getPhotoUrls(listingCreateRequestDTO.getMultipartFiles()));
            listingRepository.save(listing);
            return listing;
        } catch (IOException exception) {
            throw new IOException("Error while uploading files", exception);
        } catch (Exception exception) {
            throw new RuntimeException("Error while creating listing");
        }
    }
    private List<String> getPhotoUrls(List<MultipartFile> photos) throws IOException {
        List<String> photoUrls = new ArrayList<>();
        for (MultipartFile photo : photos) {
            String photoUrl = googleCloudStorageService.uploadFile(photo);
            photoUrls.add(photoUrl);
        }
        return photoUrls;
    }
}
