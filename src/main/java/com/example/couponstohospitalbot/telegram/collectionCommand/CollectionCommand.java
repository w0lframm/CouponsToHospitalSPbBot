package com.example.couponstohospitalbot.telegram.collectionCommand;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.model.CollectionService;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class CollectionCommand implements Command {
    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(CollectionCommand.class.getName());

    @Override
    public void execute(Update update) {
        Long chatId;
        try {
            chatId = update.getMessage().getChatId();
        } catch (NullPointerException e) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        logger.info("ChatId = " + chatId + "; getCollection");

        try {
            message = new SendMessage(chatId.toString(), "Завершённые отслеживания:\n" +
                    ApplicationContextHolder.getContext().getBean(CollectionService.class).getVisitInfo(chatId) +
                    "\nНажмите на кнопку для выбора действия");
            message.enableHtml(true);
            message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(CollectionService.class).getCollection(chatId));
            sender.execute(message);
        } catch (TelegramApiException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
