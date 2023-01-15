package com.example.couponstohospitalbot.telegram.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, Long> {
    @Query("select t from Tracking t where t.isFinished = false")
    List<Tracking> getActiveRequests();
}
