package com.example.couponstohospitalbot.telegram.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, Long> {
    @Query("select t from Tracking t where t.isFinished = false")
    List<Tracking> getActiveRequests();

    @Query("select t from Tracking t where t.chatId = ?1 and t.isFinished = true")
    List<Tracking> listFinishedTrackIdByChatId(Long chatId);

    @Query("select t from Tracking t where t.chatId = ?1 and t.regionId = ?2 and t.hospitalId = ?3 and t.directionId = ?4 and t.doctorId = ?5")
    Optional<Tracking> findEqual(long chatId, String regionId, Integer hospitalId, String directionId, String doctorId);
}
