package com.example.couponstohospitalbot.telegram.command;

import com.example.couponstohospitalbot.telegram.Command;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.START_MESSAGE;

@RequiredArgsConstructor
public class StartCommand implements Command {
    private final MessageSender sender;
    SendMessage message;

    // Здесь не добавляем сервис через получение из Application Context.
    // Потому что если это сделать так, то будет циклическая зависимость, которая
    // ломает работу приложения.

    @Override
    public void execute(Update update) {
        message = new SendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
