package com.example.couponstohospitalbot.telegram.service;

public interface SendBotMessageService {
    void sendMessage(String chatId, String message);
}
