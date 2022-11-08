package com.example.couponstohospitalbot.telegram.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandName {

    START("/start"),
    HELP("/help"),
    NO("nocommand"),
    STOP("/stop"),
    CHOOSE("/choose");

    private final String commandName;
}
