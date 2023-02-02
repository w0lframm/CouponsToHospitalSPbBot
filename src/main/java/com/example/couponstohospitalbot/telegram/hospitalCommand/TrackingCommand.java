package com.example.couponstohospitalbot.telegram.hospitalCommand;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.exception.SiteFailException;
import com.example.couponstohospitalbot.telegram.keyboards.KeyBoardFactory;
import com.example.couponstohospitalbot.telegram.model.State;
import com.example.couponstohospitalbot.telegram.model.StateService;
import com.example.couponstohospitalbot.telegram.model.TrackingService;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.BACK;
import static com.example.couponstohospitalbot.telegram.keyboards.Constants.WAIT_MESSAGE;

@RequiredArgsConstructor
public class TrackingCommand implements Command {
    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(TrackingCommand.class.getName());

    @Override
    public void execute(Update update) throws SiteFailException {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        State state = ApplicationContextHolder.getContext().getBean(StateService.class).findByChatId(chatId);
        Long trackId = ApplicationContextHolder.getContext().getBean(TrackingService.class).initTracking(state);
        String data = update.getCallbackQuery().getData();

        if(Objects.equals(data, BACK)) {
            logger.info("ChatId = " + chatId + "; Нажата кнопка Назад");
            try {
                message = new SendMessage(chatId.toString(), "Повторите выбор доктора: ");
                message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).doctorButtons(chatId, BACK));
                message.enableHtml(true);
                sender.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContextHolder.getContext().getBean(TrackingService.class).addEvent(trackId); //добавляем в отслеживание еще одно событие
            try {
                message = new SendMessage(chatId.toString(),
                        ApplicationContextHolder.getContext().getBean(StateService.class).getRequestInfo(chatId) + "\n" + WAIT_MESSAGE);
                message.enableHtml(true);
                sender.execute(message);
            } catch (TelegramApiException | URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
