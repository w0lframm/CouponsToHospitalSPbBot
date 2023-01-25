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
    TRACKING("/tracking");

    private final String hospitalCommandName;
}
