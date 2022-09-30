package com.example.couponstohospitalbot.telegram.hospitalCommand;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HospitalCommandName {
    // Выберите район
    CHOOSE_AREA("/choose_area"),
    // Выберите поликлинику
    CHOOSE_HOSPITAL("/choose_hospital"),
    // Выберите отделение
    CHOOSE_DEPARTMENT("/choose_department"),
    // Выберите врача
    CHOOSE_DOCTOR("choose_doctor"),
    // Подтвердите правильность данных
    SUBMIT("/submit");

    private final String hospitalCommandName;
}
