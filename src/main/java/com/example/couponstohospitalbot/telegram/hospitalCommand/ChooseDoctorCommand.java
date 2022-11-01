package com.example.couponstohospitalbot.telegram.hospitalCommand;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.keyboards.KeyBoardFactory;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import static com.example.couponstohospitalbot.telegram.keyboards.Constants.CHOOSE_DOCTOR;

@RequiredArgsConstructor
public class ChooseDoctorCommand implements Command {
    private final MessageSender sender;
    SendMessage message;

    @Override
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        message = new SendMessage(chatId.toString(), CHOOSE_DOCTOR);
        //переделать
        try {

            //System.out.println(update.getCallbackQuery().getData());
            message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).doctorButtons(chatId, update.getCallbackQuery().getData())); // надо бы выводить еще количество талонов, если они есть
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
