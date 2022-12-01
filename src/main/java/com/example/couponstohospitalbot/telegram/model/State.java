package com.example.couponstohospitalbot.telegram.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name="state")
@NoArgsConstructor
public class State {
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "region_id")
    private String regionId;
    @Column(name = "hospital_id")
    private Integer hospitalId;
    @Column(name = "direction_id")
    private String directionId;
    @Column(name = "doctor_id")
    private String doctorId;

    public State(Long chatId) {
        this.chatId = chatId;
        regionId = null;
        hospitalId = null;
        directionId = null;
        doctorId = null;
    }
}
