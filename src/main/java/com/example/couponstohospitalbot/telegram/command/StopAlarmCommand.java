package com.example.couponstohospitalbot.telegram.command;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.model.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
public class StopAlarmCommand implements Command {
    private final MessageSender sender;
    SendMessage message;

    @SneakyThrows
    @Override
    public void execute(Update update) {
        try {
            message = new SendMessage(update.getMessage().getChatId().toString(), "Оповещения остановлены");
            ApplicationContextHolder.getContext().getBean(TrackingService.class).alarmStop();
            message.enableHtml(true);
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}

