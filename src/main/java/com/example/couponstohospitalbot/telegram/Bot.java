package com.example.couponstohospitalbot.telegram;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.command.CommandContainer;
import com.example.couponstohospitalbot.telegram.hospitalCommand.HospitalCommandContainer;

import com.example.couponstohospitalbot.telegram.hospitalCommand.HospitalCommandName;
import com.example.couponstohospitalbot.telegram.hospitalCommand.NotifyCommand;
import com.example.couponstohospitalbot.telegram.model.StateService;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.sender.DefaultSender;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.command.CommandName.*;
import static com.example.couponstohospitalbot.telegram.hospitalCommand.HospitalCommandName.TRACKING;

@Component
public class Bot extends TelegramLongPollingBot {
    public static String COMMAND_PREFIX = "/";

    private final BotProperties botProperties;
    private final CommandContainer commandContainer;
    private final HospitalCommandContainer hospitalCommandContainer;
    private static final Logger logger = Logger.getLogger(Bot.class.getName());
    MessageSender sender;


    public Bot(BotProperties botProperties) {
        this.botProperties = botProperties;
        sender = new DefaultSender(this);
        this.commandContainer = new CommandContainer(sender);
        this.hospitalCommandContainer = new HospitalCommandContainer(sender);

        // команды появляются в меню
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(START.getCommandName(), "get a welcome message"));
        listOfCommands.add(new BotCommand(STOP.getCommandName(), "get your data stored"));
        listOfCommands.add(new BotCommand(HELP.getCommandName(), "info how to use this bot"));
        listOfCommands.add(new BotCommand(CHOOSE.getCommandName(), "choose your region"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void notifyUser(String chatId, String message) {
        new NotifyCommand(sender).execute(chatId, message);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                String message = update.getMessage().getText().trim();
                if (message.startsWith(COMMAND_PREFIX)) {
                    String commandIdentifier = message.split(" ")[0].toLowerCase();
                    commandContainer.retrieveCommand(commandIdentifier).execute(update);
                } else {
                    commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
                }
            }
        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (update.getCallbackQuery().getData().equals(TRACKING.getHospitalCommandName())) {
                hospitalCommandContainer.retrieveCommand(TRACKING.getHospitalCommandName()).execute(update);
            } else {
                HospitalCommandName name = ApplicationContextHolder.getContext().getBean(StateService.class).getCurrentState(chatId);
                hospitalCommandContainer.retrieveCommand(name.getHospitalCommandName()).execute(update);
            }
        }
    }


    @Override
    public void onClosing() { //?? работает ли?
        super.onClosing();
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }
}
