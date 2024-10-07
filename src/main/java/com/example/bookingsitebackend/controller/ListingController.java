package com.example.bookingsitebackend.controller;

import com.example.bookingsitebackend.dto.ListingCreateRequestDTO;
import com.example.bookingsitebackend.dto.ListingFilterDTO;
import com.example.bookingsitebackend.entity.Listing;
import com.example.bookingsitebackend.factory.ListingFactory;
import com.example.bookingsitebackend.repository.ListingRepository;
import com.example.bookingsitebackend.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/listing")
public class ListingController {
    @Autowired
    private ListingService listingService;
    @Autowired
    private ListingRepository listingRepository;
    @GetMapping("/filter")
    public ResponseEntity<List<Listing>> filterListing(@RequestParam ListingFilterDTO listingDTO) {
        List<Listing> listings = listingService.getListingWithFilters(listingDTO.getCity(), listingDTO.getMinPrice(), listingDTO.getMaxPrice(), listingDTO.getRooms());
        return ResponseEntity.ok(listings);
    }
    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@ModelAttribute ListingCreateRequestDTO createRequestDTO) throws IOException {
        try {
            listingService.createListing(createRequestDTO);
            return ResponseEntity.ok("Listing created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading files");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while creating listing");
        }
    }

    @GetMapping("/getByCity/{city}")
    public ResponseEntity<List<Listing>> getListingsByCity(@PathVariable String city) {
        List<Listing> listings = listingRepository.findAllByCity(city);
        return ResponseEntity.ok(listings);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable int id) {
        Optional<Listing> roomListing = listingRepository.findById(id);
        return roomListing.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    @DeleteMapping("/delete/{path:[\\d]+(?:-[\\d]+)?}")
    public ResponseEntity<String> deleteListings(@PathVariable String path) {
        return listingService.deleteListing(path);
    }

}

