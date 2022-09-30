package com.example.couponstohospitalbot.telegram.hospitalCommand;

import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.command.*;
import com.example.couponstohospitalbot.telegram.service.SendBotMessageService;
import com.google.common.collect.ImmutableMap;

import static com.example.couponstohospitalbot.telegram.command.CommandName.*;

public class HospitalCommandContainer {

        private final ImmutableMap<String, Command> hospitalCommandMap;
        private final Command unknownCommand;

        public HospitalCommandContainer(SendBotMessageService sendBotMessageService) {

            hospitalCommandMap = ImmutableMap.<String, Command> builder()
                    .put(CHOOSE.getCommandName(), new StartCommand(sendBotMessageService))
                    .build();

            unknownCommand = new UnknownCommand(sendBotMessageService);
        }


    public Command retrieveCommand(String commandIdentifier) {
            return hospitalCommandMap.getOrDefault(commandIdentifier, unknownCommand);
        }

}
