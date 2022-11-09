package com.example.couponstohospitalbot.telegram.hospitalCommand;

import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
public class NotifyCommand {
    private final MessageSender sender;
    SendMessage message;

    public void execute(String chatId, String mess) {
        try {
            message = new SendMessage(chatId, mess);
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
