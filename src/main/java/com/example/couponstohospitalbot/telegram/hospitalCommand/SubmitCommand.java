package com.example.couponstohospitalbot.telegram.hospitalCommand;

import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.keyboards.KeyBoardFactory;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class SubmitCommand implements Command {
    private final MessageSender sender;
    SendMessage message;
    KeyBoardFactory factory;

    @Override
    public void execute(Update update) {
        //сделать
    }
}
