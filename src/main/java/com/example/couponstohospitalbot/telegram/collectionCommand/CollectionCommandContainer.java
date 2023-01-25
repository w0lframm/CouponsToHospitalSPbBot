package com.example.couponstohospitalbot.telegram.collectionCommand;

import com.example.couponstohospitalbot.telegram.Command;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;

import static com.example.couponstohospitalbot.telegram.collectionCommand.CollectionCommandName.*;

@RequiredArgsConstructor
public class CollectionCommandContainer {
    private final ImmutableMap<String, Command> commandMap;

    public CollectionCommandContainer(MessageSender sender) {

        commandMap = ImmutableMap.<String, Command> builder()
                .put(CHOOSE_ITEM.getCommandName(), new ChooseItemCommand(sender))
                .put(BACK.getCommandName(), new CollectionCommand(sender))
                .put(SUBMIT.getCommandName(), new SubmitColCommand(sender))
                .put(DELETE_ITEM.getCommandName(), new DeleteItemCommand(sender))
                .put(TRACKING_ITEM.getCommandName(), new TrackingItemCommand(sender))
                .build();
    }

    public Command retrieveCommand(String name) {
        String[] parts = name.split(" ");
        return commandMap.get(parts[0] + " ");
    }

}
