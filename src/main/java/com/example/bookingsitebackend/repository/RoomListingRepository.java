package com.example.bookingsitebackend.repository;

import com.example.bookingsitebackend.entity.RoomListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomListingRepository extends JpaRepository<RoomListing, Integer> {
    @Query("SELECT r FROM RoomListing r WHERE ST_Distance(r.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) < :distance")
    List<RoomListing> findRoomListingsByNearLocation(@Param("latitude") double latitude, @Param("longitude") double longitude, @Param("distance") double distance);
}
