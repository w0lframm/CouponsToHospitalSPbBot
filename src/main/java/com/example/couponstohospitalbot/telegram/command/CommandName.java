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
    // выбираем район - выбираем поликлинику - отделение врача - врач
    // todo - добавить дату

    CHOOSE("/choose");

    private final String commandName;
}
