package com.example.couponstohospitalbot.telegram.command;

import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.service.SendBotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class StartCommand implements Command {
    private final SendBotMessageService sendBotMessageService;

    public final static String START_MESSAGE = "Привет. Я помогу тебе узнать о новых талончиках в твою поликлинничечку. " +
            "Но я еще маленький и только учусь.\n"+
            "Если хочешь выбрать новую поликлинику нажми";

    // Здесь не добавляем сервис через получение из Application Context.
    // Потому что если это сделать так, то будет циклическая зависимость, которая
    // ломает работу приложения.

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}
