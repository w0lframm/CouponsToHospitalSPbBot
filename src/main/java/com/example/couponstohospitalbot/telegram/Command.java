package com.example.couponstohospitalbot.telegram;

import com.example.couponstohospitalbot.telegram.exception.SiteFailException;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    void execute(Update update) throws SiteFailException;
}
