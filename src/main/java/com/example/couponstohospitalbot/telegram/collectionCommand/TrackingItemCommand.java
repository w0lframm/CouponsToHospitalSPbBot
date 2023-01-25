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
public class TrackingItemCommand implements Command {
    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(TrackingItemCommand.class.getName());
    @Override
    public void execute(Update update) {
        String[] parts = update.getCallbackQuery().getData().split(" ");
        long trackId = Long.parseLong(parts[1]);
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        logger.info("ChatId = " + chatId + "; track item: id = " + trackId);

        try {
            ApplicationContextHolder.getContext().getBean(CollectionService.class).trackItem(trackId);
            message = new SendMessage(chatId.toString(),
                    ApplicationContextHolder.getContext().getBean(TrackingService.class).getRequestInfo(trackId) +
                            "\nЗапрос начал отслеживаться. Я пришлю уведомление, как только появятся талоны!");
            sender.execute(message);
        } catch (TelegramApiException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
