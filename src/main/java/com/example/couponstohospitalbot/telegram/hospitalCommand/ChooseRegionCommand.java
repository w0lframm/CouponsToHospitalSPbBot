package com.example.couponstohospitalbot.telegram.hospitalCommand;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.exception.SiteFailException;
import com.example.couponstohospitalbot.telegram.keyboards.KeyBoardFactory;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.CHOOSE_MESSAGE;

@RequiredArgsConstructor
public class ChooseRegionCommand implements Command {

    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(ChooseRegionCommand.class.getName());

    @Override
    public void execute(Update update) throws SiteFailException {
        Long chatId = update.getMessage().getChatId();
        logger.info("ChatId = " + chatId);
        message = new SendMessage(chatId.toString(), CHOOSE_MESSAGE);
        try {
            message.enableHtml(true);
            message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).regionButtons(chatId, false));
            sender.execute(message);
        } catch (IOException | URISyntaxException | TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
