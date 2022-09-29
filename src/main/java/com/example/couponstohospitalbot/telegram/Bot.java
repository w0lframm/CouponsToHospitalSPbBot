package com.example.couponstohospitalbot.telegram;

import com.example.couponstohospitalbot.telegram.command.CommandContainer;
import com.example.couponstohospitalbot.telegram.service.SendBotMessageServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

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
}
