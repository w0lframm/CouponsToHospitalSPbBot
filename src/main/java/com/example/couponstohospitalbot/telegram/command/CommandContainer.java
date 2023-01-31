package com.example.couponstohospitalbot.telegram.command;

import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.collectionCommand.CollectionCommand;
import com.example.couponstohospitalbot.telegram.hospitalCommand.ChooseRegionCommand;
import com.google.common.collect.ImmutableMap;
import org.telegram.abilitybots.api.sender.MessageSender;

import static com.example.couponstohospitalbot.telegram.command.CommandName.*;

public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(MessageSender sender) {

        commandMap = ImmutableMap.<String, Command> builder()
                .put(START.getCommandName(), new StartCommand(sender))
                .put(STOP.getCommandName(), new StopCommand(sender))
                .put(HELP.getCommandName(), new HelpCommand(sender))
                .put(NO.getCommandName(), new NoCommand(sender))
                .put(CHOOSE.getCommandName(), new ChooseRegionCommand(sender))
                .put(COLLECTION.getCommandName(), new CollectionCommand(sender))
                .put(STOP_ALARM.getCommandName(), new StopAlarmCommand(sender))
                .build();

        unknownCommand = new UnknownCommand(sender);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}