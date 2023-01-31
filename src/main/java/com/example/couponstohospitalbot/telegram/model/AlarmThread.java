package com.example.couponstohospitalbot.telegram.model;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Bot;
import lombok.SneakyThrows;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.ANSWER_MESSAGE;

public class AlarmThread extends Thread {

    private final Integer numNotify = 25;
    private final String chatId;

    public AlarmThread(String chatId) {
        this.chatId = chatId;
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            for (int k = 0; k < numNotify && !isInterrupted(); ++k) {
                Integer messageId = ApplicationContextHolder.getContext().getBean(Bot.class).notifyUser(chatId, ANSWER_MESSAGE);
                Thread.sleep(3000);
                ApplicationContextHolder.getContext().getBean(Bot.class).deleteLastNotifyMsg(chatId, messageId);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
