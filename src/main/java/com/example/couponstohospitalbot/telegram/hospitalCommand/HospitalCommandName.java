package com.example.couponstohospitalbot.telegram.hospitalCommand;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HospitalCommandName {
    REGION("/region"),
    HOSPITAL("/hospital"),
    DIRECTION("/direction"),
    DOCTOR("/doctor"),
    // Подтвердите правильность данных
    SUBMIT("/submit"), //доделать
    NO("/no"); //доделать

    private final String hospitalCommandName;
}
