package com.example.couponstohospitalbot.telegram.hospitalCommand;

import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class ChooseCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public static final String CHOOSE_MESSAGE = "начать выбор \uD83D\uDE1F.";

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), CHOOSE_MESSAGE);
    }
}
