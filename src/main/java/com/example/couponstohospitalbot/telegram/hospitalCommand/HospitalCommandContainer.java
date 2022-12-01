package com.example.couponstohospitalbot.telegram.hospitalCommand;

import com.example.couponstohospitalbot.telegram.Command;
import com.example.couponstohospitalbot.telegram.command.*;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.MessageSender;

import static com.example.couponstohospitalbot.telegram.hospitalCommand.HospitalCommandName.*;

@RequiredArgsConstructor
public class HospitalCommandContainer {

    private final ImmutableMap<String, Command> commandMap;


    public HospitalCommandContainer(MessageSender sender) {

        commandMap = ImmutableMap.<String, Command> builder()
                .put(REGION.getHospitalCommandName(), new ChooseHospitalCommand(sender))
                .put(HOSPITAL.getHospitalCommandName(), new ChooseDirectionCommand(sender))
                .put(DIRECTION.getHospitalCommandName(), new ChooseDoctorCommand(sender))
                .put(DOCTOR.getHospitalCommandName(), new SubmitCommand(sender))
                .put(NO.getHospitalCommandName(), new NoCommand(sender))
                .put(TRACKING.getHospitalCommandName(), new TrackingCommand(sender))
                .build();
    }

    public Command retrieveCommand(String name) {
        return commandMap.get(name);
    }
}
