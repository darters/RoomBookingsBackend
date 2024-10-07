package com.example.bookingsitebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_listings")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    @ElementCollection
    @CollectionTable(name = "photo_urls", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "photo_url")    private List<String> photosUrl;
    private int numberOfRooms;
    private int roomRating;
    private double pricePerDay;
    private double latitude;
    private double longitude;
    private String address;
    private String city;
    private float distanceFromCenter;
}

