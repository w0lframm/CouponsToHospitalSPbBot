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
import java.util.Objects;
import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.CHOOSE_DIRECTION;
import static com.example.couponstohospitalbot.telegram.keyboards.ParsingJson.findHospitalNameById;

@RequiredArgsConstructor
public class ChooseDirectionCommand implements Command {
    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(ChooseDirectionCommand.class.getName());
    private static final String BACK = "Назад";

    @Override
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String hospitalId = update.getCallbackQuery().getData();
        if(Objects.equals(hospitalId, BACK)) {
            logger.info("ChatId = " + chatId + "; Нажата кнопка Назад");
            try {
                message = new SendMessage(chatId.toString(), "Повторите выбор района: ");
                message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).regionButtons(chatId, true));
                sender.execute(message);
            } catch (TelegramApiException | URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("ChatId = " + chatId + "; HospitalId = " + hospitalId);
            try {
                message = new SendMessage(chatId.toString(), "Больница: " + findHospitalNameById(chatId, hospitalId) + CHOOSE_DIRECTION);
                message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).departmentButtons(chatId, hospitalId));
                sender.execute(message);
            } catch (TelegramApiException | IOException | URISyntaxException e) {
                    e.printStackTrace();
            }
        }
    }
}
