package com.example.couponstohospitalbot.telegram.command;

import com.example.couponstohospitalbot.telegram.Command;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.STOP_MESSAGE;

@RequiredArgsConstructor
public class StopCommand implements Command {

    private final MessageSender sender;
    SendMessage message;

    @Override
    public void execute(Update update) {
        message = new SendMessage(update.getMessage().getChatId().toString(), STOP_MESSAGE);
        try {
            sender.execute(message); //здесь надо как то сбросить текущее состояние выбора
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
