package com.example.couponstohospitalbot.model;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name="visit") // пока никуда не подключаем
public class Visit {
    @Id
    @Column(name = "visit_id", nullable = false)
    private Long id;

}
