package com.example.couponstohospitalbot.telegram.collectionCommand;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.exception.SiteFailException;
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
public class DeleteItemCommand implements Command {
    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(DeleteItemCommand.class.getName());
    @Override
    public void execute(Update update) throws SiteFailException {
        String[] parts = update.getCallbackQuery().getData().split(" ");
        long trackId = Long.parseLong(parts[1]);
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        logger.info("ChatId = " + chatId + "; delete item id = " + trackId);

        try {
            message = new SendMessage(chatId.toString(),
                    ApplicationContextHolder.getContext().getBean(TrackingService.class).getRequestInfo(trackId) +
                            "\nЗапрос удален из коллекции.");
            ApplicationContextHolder.getContext().getBean(CollectionService.class).deleteItem(trackId);
            sender.execute(message);
        } catch (TelegramApiException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
