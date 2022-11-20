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

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.CHOOSE_DOCTOR;
import static com.example.couponstohospitalbot.telegram.keyboards.ParsingJson.findDirectionNameById;

@RequiredArgsConstructor
public class ChooseDoctorCommand implements Command {
    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(ChooseDoctorCommand.class.getName());
    private static final String BACK = "Назад";

    @Override
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String directionId = update.getCallbackQuery().getData();
        if(Objects.equals(directionId, BACK)) {
            logger.info("ChatId = " + chatId + "; Нажата кнопка Назад");
            try {
                message = new SendMessage(chatId.toString(), "Повторите выбор больницы: ");
                message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).hospitalButtons(chatId, BACK));
                sender.execute(message);
            } catch (TelegramApiException | URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("ChatId = " + chatId + "; DirectionId = " + directionId);
            try {
                message = new SendMessage(chatId.toString(), "Направление: " + findDirectionNameById(chatId, directionId) + CHOOSE_DOCTOR);
                message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).doctorButtons(chatId, directionId));
                sender.execute(message);
            } catch (TelegramApiException | URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
