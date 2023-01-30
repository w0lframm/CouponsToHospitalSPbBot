package com.example.couponstohospitalbot.telegram.model;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Bot;
import lombok.SneakyThrows;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.ANSWER_MESSAGE;

public class AlarmThread extends Thread {

    private final Integer numNotify = 20;
    private final String chatId;

    public AlarmThread(String chatId) {
        this.chatId = chatId;
    }

    @SneakyThrows
    @Override
    public void run() {
        for (int k = 0; k < numNotify; ++k) {
           Integer messageId = ApplicationContextHolder.getContext().getBean(Bot.class).notifyUser(chatId, ANSWER_MESSAGE);
            Thread.sleep(3000);
            ApplicationContextHolder.getContext().getBean(Bot.class).deleteLastNotifyMsg(chatId, messageId);
        }
        System.out.println("END");
    }
}
