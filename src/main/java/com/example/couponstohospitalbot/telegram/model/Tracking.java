package com.example.couponstohospitalbot.telegram.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="tracking")
@NoArgsConstructor
public class Tracking {
    @Id
    @GeneratedValue
    @Column(name = "track_id", nullable = false)
    private Long trackId;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "region_id", nullable = false)
    private String regionId;
    @Column(name = "hospital_id", nullable = false)
    private Integer hospitalId;
    @Column(name = "direction_id", nullable = false)
    private String directionId;
    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "is_finished", nullable = false) //отработал или еще нет
    private Boolean isFinished;

    public Tracking(State state) {
        chatId = state.getChatId();
        regionId =state.getRegionId();
        hospitalId = state.getHospitalId();
        directionId = state.getDirectionId();
        doctorId = state.getDoctorId();
        isFinished = false;
    }
}
