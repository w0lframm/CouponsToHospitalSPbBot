package com.example.couponstohospitalbot.telegram.command;

import com.example.couponstohospitalbot.telegram.Command;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.UNKNOWN_MESSAGE;

@RequiredArgsConstructor
public class UnknownCommand implements Command {

    private final MessageSender sender;
    SendMessage message;

    @Override
    public void execute(Update update) {
        message = new SendMessage(update.getMessage().getChatId().toString(), UNKNOWN_MESSAGE);
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}