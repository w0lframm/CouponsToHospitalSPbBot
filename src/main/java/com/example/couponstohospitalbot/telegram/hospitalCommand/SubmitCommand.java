package com.example.couponstohospitalbot.telegram.hospitalCommand;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.keyboards.KeyBoardFactory;
import com.example.couponstohospitalbot.telegram.model.StateService;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.*;

@RequiredArgsConstructor
public class SubmitCommand implements Command {

    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(SubmitCommand.class.getName());
    private static final String ALL_DOCTORS = "Без разницы";

    @Override
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String doctorId = update.getCallbackQuery().getData();
        if(Objects.equals(doctorId, BACK)) {
            logger.info("ChatId = " + chatId + "; Нажата кнопка Назад");
            try {
                message = new SendMessage(chatId.toString(), "Повторите выбор отделения: ");
                message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).departmentButtons(chatId, BACK));
                sender.execute(message);
            } catch (TelegramApiException | URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                message = new SendMessage(chatId.toString(), CONFIRM_MESSAGE +
                        ApplicationContextHolder.getContext().getBean(StateService.class).getRequestInfo(chatId, doctorId));
                message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).submitButton(chatId, doctorId));
                sender.execute(message);
            } catch (TelegramApiException | URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
