package com.example.couponstohospitalbot.telegram.hospitalCommand;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.keyboards.KeyBoardFactory;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.CHOOSE_DIRECTION;

@RequiredArgsConstructor
public class ChooseDirectionCommand implements Command {
    private final MessageSender sender;
    SendMessage message;


    @Override
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        message = new SendMessage(chatId.toString(), CHOOSE_DIRECTION);
        try {
            message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).departmentButtons(chatId, update.getCallbackQuery().getData()));
            sender.execute(message);
        } catch (TelegramApiException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
