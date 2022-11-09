package com.example.couponstohospitalbot.telegram.hospitalCommand;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.model.State;
import com.example.couponstohospitalbot.telegram.model.StateService;
import com.example.couponstohospitalbot.telegram.model.TrackingService;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.WAIT_MESSAGE;

@RequiredArgsConstructor
public class TrackingCommand implements Command{
    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(TrackingCommand.class.getName());

    @Override
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        State state = ApplicationContextHolder.getContext().getBean(StateService.class).findByChatId(chatId);
        Long trackId = ApplicationContextHolder.getContext().getBean(TrackingService.class).initTracking(state);
        ApplicationContextHolder.getContext().getBean(TrackingService.class).addEvent(trackId); //добавляем в отслеживание еще одно событие
        try {
            message = new SendMessage(chatId.toString(), WAIT_MESSAGE);
            sender.execute(message);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
