package com.example.couponstohospitalbot.telegram;

import com.example.couponstohospitalbot.telegram.command.CommandContainer;
import com.example.couponstohospitalbot.telegram.keyboards.Buttons;
import com.example.couponstohospitalbot.telegram.service.SendBotMessageServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.command.CommandName.NO;

@Component
public class Bot extends TelegramLongPollingBot {
    public static String COMMAND_PREFIX = "/";
    private static final Logger log = Logger.getLogger(Bot.class.getName());
    private final CommandContainer commandContainer;
    private final BotProperties botProperties;

    public Bot(BotProperties botProperties) {
        this.botProperties = botProperties;
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this));
        // команды появляются в меню
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "get a welcome message"));
        listofCommands.add(new BotCommand("/stop", "get your data stored"));
        listofCommands.add(new BotCommand("/help", "info how to use this bot"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            if (message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }
        }
    }

    public void setButtonsDist(Long chatId) throws URISyntaxException, IOException {
        SendMessage sendMessage = Buttons.setButtonsDistrict(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void setButtonsHospital(Long chatId, String message) throws URISyntaxException, IOException {
        SendMessage sendMessage = Buttons.setButtonsHospital(chatId, message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void setButtonsDirection(Long chatId, String message) throws URISyntaxException, IOException {
        SendMessage sendMessage = Buttons.setButtonsDirection(chatId, message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void setButtonsDoctors(Long chatId, String message) throws URISyntaxException, IOException {
        SendMessage sendMessage = Buttons.setButtonDoctors(chatId, message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void setButtonsTime(Long chatId, String message) throws URISyntaxException, IOException {
        SendMessage sendMessage = Buttons.setButtonDateAndTime(chatId, message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
