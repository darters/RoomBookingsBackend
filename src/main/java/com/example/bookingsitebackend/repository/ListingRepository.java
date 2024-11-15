package com.example.bookingsitebackend.repository;

import com.example.bookingsitebackend.entity.Listing;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer>, JpaSpecificationExecutor<Listing> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Listing r WHERE r.id BETWEEN :startRoomId and :endRoomId")
    void deleteListingsByRange(@Param("startRoomId") int startRoomId, @Param("endRoomId") int endRoomId);
    List<Listing> findAllByCity(String city);
}
