package com.example.couponstohospitalbot.telegram;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.collectionCommand.CollectionCommandContainer;
import com.example.couponstohospitalbot.telegram.command.CommandContainer;
import com.example.couponstohospitalbot.telegram.command.FailSiteCommand;
import com.example.couponstohospitalbot.telegram.exception.SiteFailException;
import com.example.couponstohospitalbot.telegram.hospitalCommand.HospitalCommandContainer;

import com.example.couponstohospitalbot.telegram.hospitalCommand.HospitalCommandName;
import com.example.couponstohospitalbot.telegram.hospitalCommand.NotifyCommand;
import com.example.couponstohospitalbot.telegram.model.StateService;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.sender.DefaultSender;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.command.CommandName.*;

@Component
public class Bot extends TelegramLongPollingBot {
    public static String COMMAND_PREFIX = "/";
    private final BotProperties botProperties;
    private final CommandContainer commandContainer;
    private final HospitalCommandContainer hospitalCommandContainer;
    private final CollectionCommandContainer collectionCommandContainer;
    private static final Logger logger = Logger.getLogger(Bot.class.getName());
    MessageSender sender;

    public Bot(BotProperties botProperties) {
        this.botProperties = botProperties;
        sender = new DefaultSender(this);
        this.commandContainer = new CommandContainer(sender);
        this.hospitalCommandContainer = new HospitalCommandContainer(sender);
        this.collectionCommandContainer = new CollectionCommandContainer(sender);

        // команды появляются в меню
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(START.getCommandName(), "get a welcome message"));
        listOfCommands.add(new BotCommand(HELP.getCommandName(), "info how to use this bot"));
        listOfCommands.add(new BotCommand(COLLECTION.getCommandName(), "choose your previous request"));
        listOfCommands.add(new BotCommand(CHOOSE.getCommandName(), "choose your region"));
        listOfCommands.add(new BotCommand(STOP.getCommandName(), "get your data stored"));
        listOfCommands.add(new BotCommand(STOP_ALARM.getCommandName(), "stop alarm when coupon found"));


        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
            logger.info("commands are set");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Integer notifyUser(String chatId, String message) {
        return new NotifyCommand(sender).execute(chatId, message);
    }

    public void deleteLastNotifyMsg(String chatId, Integer messageId) {
        new NotifyCommand(sender).deleteLastMsg(chatId, messageId);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                String message = update.getMessage().getText().trim();
                try {
                    if (message.startsWith(COMMAND_PREFIX)) {
                        String commandIdentifier = message.split(" ")[0].toLowerCase();
                            commandContainer.retrieveCommand(commandIdentifier).execute(update);
                    } else {
                        commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
                    }
                } catch (JSONException | SiteFailException e) {
                    new FailSiteCommand(sender).execute(update);
                }
            }
        } else if (update.hasCallbackQuery()) {
            DeleteMessage deleteMessage = new DeleteMessage();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            deleteMessage.setChatId(chatId.toString());
            deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            try {
                execute(deleteMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            try {
                //проверка, что мы в коллекции
                if (update.getCallbackQuery().getData().startsWith("/col_")) {
                    collectionCommandContainer.retrieveCommand(update.getCallbackQuery().getData()).execute(update);
                } else {
                    HospitalCommandName name = ApplicationContextHolder.getContext().getBean(StateService.class).getCurrentState(chatId);
                    hospitalCommandContainer.retrieveCommand(name.getHospitalCommandName()).execute(update);
                }
            } catch (JSONException | SiteFailException e) {
                new FailSiteCommand(sender).execute(update);
            }
        }
    }


    @Override
    public void onClosing() {
        super.onClosing();
    } // работает ли?

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }
}
