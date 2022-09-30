package com.example.couponstohospitalbot.telegram.command;

import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class StopCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public static final String STOP_MESSAGE = "Деактивировал все ваши подписки \uD83D\uDE1F.";

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), STOP_MESSAGE);
    }
}
