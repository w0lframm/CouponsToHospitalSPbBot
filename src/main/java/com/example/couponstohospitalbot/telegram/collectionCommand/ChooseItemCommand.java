package com.example.couponstohospitalbot.telegram.collectionCommand;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.model.CollectionService;
import com.example.couponstohospitalbot.telegram.model.TrackingService;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;
@RequiredArgsConstructor
public class ChooseItemCommand implements Command {
    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(ChooseItemCommand.class.getName());
    @Override
    public void execute(Update update) { //здесь выводить 3 кнопки: отследить, удалить из коллекции, назад
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        logger.info("ChatId = " + chatId + "; choose action buttons");

        try { //работает или нет?
            String[] parts = update.getCallbackQuery().getData().split(" ");
            long trackId = Long.parseLong(parts[1]);

            message = new SendMessage(chatId.toString(), "Что вы хотите сделать?\n" +
                    ApplicationContextHolder.getContext().getBean(TrackingService.class).getRequestInfo(trackId));
            message.enableHtml(true);
            message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(CollectionService.class).getChoosingButtons(trackId));
            sender.execute(message);
        } catch (TelegramApiException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
