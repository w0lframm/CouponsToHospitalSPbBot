package com.example.couponstohospitalbot.telegram.exception;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.FAIL_MESSAGE;

public class SiteFailException extends Exception {
    public SiteFailException() {
        super(FAIL_MESSAGE);
    }
}
