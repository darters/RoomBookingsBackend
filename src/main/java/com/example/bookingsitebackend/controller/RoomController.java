package com.example.bookingsitebackend.controller;

import com.example.bookingsitebackend.entity.RoomListing;
import com.example.bookingsitebackend.repository.RoomListingRepository;
import com.example.bookingsitebackend.service.GoogleCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomListingRepository roomRepository;
    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;
    @PostMapping("/create")
    public void createRoom(@RequestPart RoomListing room, @RequestPart("photos") List<MultipartFile> multipartFiles) throws IOException {
        List<String> photoUrls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            String photoUrl = googleCloudStorageService.uploadFile(file);
            photoUrls.add(photoUrl);
        }
        System.out.println("ROOM");
        System.out.println(room);
        room.setPhotosUrl(photoUrls);
        roomRepository.save(room);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<RoomListing> getRoomById(@PathVariable int id) {
        Optional<RoomListing> roomListing = roomRepository.findById(id);
        return roomListing.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    @GetMapping("/getAll")
    public List<RoomListing> getAllHotels() {
        System.out.println("GET ALL HOTELS METHOD");
        List<RoomListing> allHotels = roomRepository.findAll();
        System.out.println(allHotels);
        return allHotels;
    }
    @DeleteMapping("/delete/{path:[\\d]+(?:-[\\d]+)?}")
    public void deleteRoom(@PathVariable String path) {
        String rangeRegex = "(\\d+)(?:-(\\d+))?";
        Pattern pattern = Pattern.compile(rangeRegex);
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            int startRoomId = Integer.parseInt(matcher.group(1));
            String endRoomIdStr = matcher.group(2);
            if (endRoomIdStr != null) {
                int endRoomId = Integer.parseInt(endRoomIdStr);
                roomRepository.deleteRoomsByRange(startRoomId, endRoomId);
                return;
            }
            roomRepository.deleteById(startRoomId);
        }
    }

}

