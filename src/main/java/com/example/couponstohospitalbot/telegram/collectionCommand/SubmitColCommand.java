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
public class SubmitColCommand implements Command {
    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(SubmitColCommand.class.getName());
    @Override
    public void execute(Update update) throws SiteFailException { // 2 кнопки: да и нет
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        logger.info("ChatId = " + chatId + "; choose submit buttons");

        try {
            String[] parts = update.getCallbackQuery().getData().split(" ");
            long trackId = Long.parseLong(parts[1]);

            message = new SendMessage(chatId.toString(), "Уверены, что хотите " + parts[2] + " выбранный пункт?\n" +
                    ApplicationContextHolder.getContext().getBean(TrackingService.class).getRequestInfo(trackId));
            String action = parts[2].equals("удалить") ? "/col_delete " : "/col_tracking ";
            message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(CollectionService.class).getSubmitButtons(trackId, action));
            sender.execute(message);
        } catch (TelegramApiException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
