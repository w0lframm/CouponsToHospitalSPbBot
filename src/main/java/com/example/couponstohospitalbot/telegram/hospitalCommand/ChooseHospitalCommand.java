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

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.CHOOSE_HOSPITAL;
import static com.example.couponstohospitalbot.telegram.keyboards.ParsingJson.findRegionNameById;

@RequiredArgsConstructor
public class ChooseHospitalCommand implements Command {
    private final MessageSender sender;
    SendMessage message;
    private static final Logger logger = Logger.getLogger(ChooseHospitalCommand.class.getName());

    @Override
    public void execute(Update update) throws SiteFailException {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String regionId = update.getCallbackQuery().getData();
        logger.info("ChatId = " + chatId + "; RegionId = " + regionId);
        try {
            message = new SendMessage(chatId.toString(), "Район: " + findRegionNameById(regionId) + CHOOSE_HOSPITAL);
            message.enableHtml(true);
            message.setReplyMarkup(ApplicationContextHolder.getContext().getBean(KeyBoardFactory.class).hospitalButtons(chatId, regionId));
            sender.execute(message);
        } catch (TelegramApiException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
