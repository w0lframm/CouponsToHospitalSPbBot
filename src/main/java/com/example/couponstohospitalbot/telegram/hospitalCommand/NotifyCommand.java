package com.example.couponstohospitalbot.telegram.hospitalCommand;

import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
public class NotifyCommand {
    private final MessageSender sender;
    SendMessage message;

    public Integer execute(String chatId, String mess) {
        try {
            message = new SendMessage(chatId, mess);
            message.enableHtml(true);
            return sender.execute(message).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteLastMsg(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try {
            sender.execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
