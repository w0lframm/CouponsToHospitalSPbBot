package com.example.couponstohospitalbot.telegram.model;

import com.example.couponstohospitalbot.telegram.hospitalCommand.HospitalCommandName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.ALL_DOCTORS;
import static com.example.couponstohospitalbot.telegram.keyboards.ParsingJson.*;

@Service
@RequiredArgsConstructor
public class StateService {
    private final StateRepository stateRepository;
    private static final Logger logger = Logger.getLogger(StateService.class.getName());

    @Transactional
    public HospitalCommandName getCurrentState(Long chatId) {
        Optional<State> optionalState = stateRepository.findByChatId(chatId);
        if (optionalState.isEmpty()) {
            logger.warning("current state is unknown");
            return null;
        }
        State state = optionalState.get();
        if (state.getRegionId() == null) {
            return HospitalCommandName.REGION;
        }
        if (state.getHospitalId() == null) {
            return HospitalCommandName.HOSPITAL;
        }
        if (state.getDirectionId() == null) {
            return HospitalCommandName.DIRECTION;
        }
        if (state.getDoctorId() == null) { //изменить для случая где ответ без разницы (например на отрицательное значение)
            return HospitalCommandName.DOCTOR;
        }

        return HospitalCommandName.TRACKING;
    }

    @Transactional
    public void saveRegion(Long chatId, String regionId) {
        Optional<State> optionalState = stateRepository.findByChatId(chatId);
        if (optionalState.isEmpty()) {
            logger.info("can't save region - chat is empty");
            return;
        }
        State state = optionalState.get();
        state.setRegionId(regionId);
        stateRepository.save(state);
    }

    @Transactional
    public void saveHospital(Long chatId, Integer hospitalId) {
        Optional<State> optionalState = stateRepository.findByChatId(chatId);
        if (optionalState.isEmpty()) {
            logger.info("can't save hospital - chat is empty");
            return;
        }
        State state = optionalState.get();
        state.setHospitalId(hospitalId);
        stateRepository.save(state);
    }

    @Transactional
    public void saveDirection(Long chatId, String directionId) {
        Optional<State> optionalState = stateRepository.findByChatId(chatId);
        if (optionalState.isEmpty()) {
            logger.info("can't save direction - chat is empty");
            return;
        }
        State state = optionalState.get();
        state.setDirectionId(directionId);
        stateRepository.save(state);
    }

    @Transactional
    public void saveDoctor(Long chatId, String doctorId) {
        Optional<State> optionalState = stateRepository.findByChatId(chatId);
        if (optionalState.isEmpty()) {
            logger.info("can't save direction - chat is empty");
            return;
        }
        State state = optionalState.get();
        state.setDoctorId(doctorId);
        stateRepository.save(state);
    }


    public State findByChatId(Long chatId) {
        Optional<State> state = stateRepository.findByChatId(chatId);
        return state.orElse(null);
    }

    @Transactional
    public void saveChat(Long chatId) {
        stateRepository.save(new State(chatId));
    }

    public String getRequestInfo(Long chatId, String doctorId) throws IOException, URISyntaxException {
        State state = findByChatId(chatId);
        StringBuilder sb = new StringBuilder("Район: ");
        sb.append(findRegionNameById(state.getRegionId())).append("\nБольница: ");
        sb.append(findHospitalNameById(chatId, state.getHospitalId().toString())).append("\nНаправление: ");
        sb.append(findDirectionNameById(chatId, state.getDirectionId())).append("\nДоктор: ");
        if (Objects.equals(doctorId, "-1")) {
            sb.append(ALL_DOCTORS);
        } else {
            sb.append(findDoctorNameById(chatId, doctorId));
        }
        return sb.toString();
    }

    public String getRequestInfo(Long chatId) throws IOException, URISyntaxException {
        State state = findByChatId(chatId);
        StringBuilder sb = new StringBuilder("Район: ");
        sb.append(findRegionNameById(state.getRegionId())).append("\nБольница: ");
        sb.append(findHospitalNameById(chatId, state.getHospitalId().toString())).append("\nНаправление: ");
        sb.append(findDirectionNameById(chatId, state.getDirectionId())).append("\nДоктор: ");
        if (state.getDoctorId().equals(ALL_DOCTORS)) {
            sb.append(ALL_DOCTORS);
        } else {
            sb.append(findDoctorNameById(chatId, state.getDoctorId()));
        }
        return sb.toString();
    }

    @Transactional
    public void saveBackHospital(Long chatId) {
        Optional<State> optionalState = stateRepository.findByChatId(chatId);
        if (optionalState.isEmpty()) {
            logger.info("can't save hospital back- chat is empty");
            return;
        }
        State state = optionalState.get();
        state.setHospitalId(null);
        stateRepository.save(state);
    }

    @Transactional
    public void saveBackRegion(Long chatId) {
        Optional<State> optionalState = stateRepository.findByChatId(chatId);
        if (optionalState.isEmpty()) {
            logger.info("can't save hospital back - chat is empty");
            return;
        }
        State state = optionalState.get();
        state.setRegionId(null);
        stateRepository.save(state);
    }

    @Transactional
    public void saveBackDirection(Long chatId) {
        Optional<State> optionalState = stateRepository.findByChatId(chatId);
        if (optionalState.isEmpty()) {
            logger.info("can't save direction back - chat is empty");
            return;
        }
        State state = optionalState.get();
        state.setDirectionId(null);
        stateRepository.save(state);
    }

    @Transactional
    public void saveBackDoctor(Long chatId) {
        Optional<State> optionalState = stateRepository.findByChatId(chatId);
        if (optionalState.isEmpty()) {
            logger.info("can't save doctor back - chat is empty");
            return;
        }
        State state = optionalState.get();
        state.setDoctorId(null);
        stateRepository.save(state);
    }
}
