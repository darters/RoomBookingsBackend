package com.example.bookingsitebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "listings")
@ToString
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    @ElementCollection
    @CollectionTable(name = "photos_urls", joinColumns = @JoinColumn(name = "listings_id"))
    @Column(name = "photos_url")
    private List<String> photosUrl;
    private int numberOfRooms;
    private int roomRating;
    private double pricePerDay;
    private double latitude;
    private double longitude;
    private String address;
    private String city;
    private float distanceFromCenter;
}

