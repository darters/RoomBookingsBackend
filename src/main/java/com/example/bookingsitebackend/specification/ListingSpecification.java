package com.example.bookingsitebackend.specification;

import com.example.bookingsitebackend.entity.Listing;
import org.springframework.data.jpa.domain.Specification;

public class ListingSpecification {
    public static Specification<Listing> priceRange(Long minPrice, Long maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("pricePerDay"), minPrice, maxPrice);
    }
    public static Specification<Listing> countOfRooms(Long rooms) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("numberOfRooms"), rooms);
    }
    public static Specification<Listing> getByCity(String city) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("city"), city);
    }
}
