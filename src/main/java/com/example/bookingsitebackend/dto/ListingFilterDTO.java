package com.example.bookingsitebackend.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ListingFilterDTO {
    private String city;
    //TODO Validate
    private Long minPrice;
    private Long maxPrice;
    private Long rooms;
}
