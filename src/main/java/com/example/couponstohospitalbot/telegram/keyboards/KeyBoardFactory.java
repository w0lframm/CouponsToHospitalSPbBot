package com.example.couponstohospitalbot.telegram.keyboards;

import com.example.couponstohospitalbot.telegram.exception.SiteFailException;
import com.example.couponstohospitalbot.telegram.model.State;
import com.example.couponstohospitalbot.telegram.model.StateService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.hospitalCommand.HospitalCommandName.TRACKING;
import static com.example.couponstohospitalbot.telegram.keyboards.Constants.ALL_DOCTORS;
import static com.example.couponstohospitalbot.telegram.keyboards.Constants.BACK;
import static com.example.couponstohospitalbot.telegram.keyboards.Shortener.*;

@Service
@RequiredArgsConstructor
public class KeyBoardFactory {

    private static final Logger logger = Logger.getLogger(KeyBoardFactory.class.getName());
    private final StateService stateService;

    public ReplyKeyboard regionButtons(Long chatId, Boolean flag) throws IOException, URISyntaxException, SiteFailException {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        JSONArray array = ParsingJson.getRegionsList();
        if(flag){
            stateService.saveBackRegion(chatId);
        }else {
            stateService.saveChat(chatId); //обновили обращение
        }
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsObj = array.getJSONObject(i);
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(jsObj.get("name").toString());
            inlineKeyboardButton.setCallbackData(jsObj.get("id").toString()); //то, что возвращается по нажатию на кнопку
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        }
        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public ReplyKeyboard hospitalButtons(Long chatId, String regionId) throws IOException, URISyntaxException, SiteFailException {
        State state = stateService.findByChatId(chatId);
        if(regionId.equals(BACK)) {
            regionId = state.getRegionId();
            stateService.saveBackHospital(chatId);
        } else {
            stateService.saveRegion(chatId, regionId); //сохранение выбора района
        }
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        JSONArray array = ParsingJson.getHospitalList(regionId);
        if (array.length() > 100) {
            logger.warning("records more than 100: " + array.length()); //смотрю чтоб не было длиннее 100 записей
        }
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsObj = array.getJSONObject(i);
            String hospName = jsObj.get("lpuFullName").toString();
            logger.info(hospName);
            if (hospName.getBytes().length > 64) {
                hospName = shortHospName(hospName); // если больше 64 байт
                if (hospName.getBytes().length > 64) {
                    hospName = jsObj.get("lpuShortName").toString();
                    if (hospName.getBytes().length > 64) {
                        hospName = shortHospName(hospName);
                    }
                }
                logger.warning("LONGER THAN 64 BYTES -> " + hospName);
            }
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(hospName); //отправляем сокращенное
            inlineKeyboardButton.setCallbackData(jsObj.get("id").toString());               //получаем id
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        }
        //добавить кнопку назад
        rowsInline.add(addBackButton());
        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }


    public ReplyKeyboard departmentButtons(Long chatId, String hospitalId) throws URISyntaxException, IOException, SiteFailException {
        State state = stateService.findByChatId(chatId);
        int hospId;
        if(hospitalId.equals(BACK)) {
            hospId = state.getHospitalId();
            stateService.saveBackDirection(chatId);
        } else {
            hospId = Integer.parseInt(hospitalId);
            stateService.saveHospital(chatId, hospId); //сохранение выбора больницы
        }
        JSONArray array = ParsingJson.getDirectionsList(hospId);
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        if (array.length() > 100) {
            logger.warning("records more than 100: " + array.length()); //смотрю чтоб не было длиннее 100 записей
        }
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsObj = array.getJSONObject(i);
            String directionName = jsObj.get("name").toString();
            logger.info("DirectionName: " + directionName);
            if (directionName.getBytes().length > 64) {
                directionName = shortDirectionName(directionName);
                logger.warning("LONGER THAN 64 BYTES -> " + directionName);
            }
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(directionName);  //(если надо то доделать сокращение)?
            inlineKeyboardButton.setCallbackData(jsObj.get("id").toString());                     //получаем id
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        }

        //добавить кнопку назад
        rowsInline.add(addBackButton());
        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public ReplyKeyboard doctorButtons(Long chatId, String directionId) throws SiteFailException {
        State state = stateService.findByChatId(chatId);
        if(directionId.equals(BACK)) {
            directionId = state.getDirectionId();
            stateService.saveBackDoctor(chatId);
        } else {
            stateService.saveDirection(chatId, directionId); //сохранение выбора направления
        }
        JSONArray array = null;
        try {
            array = ParsingJson.getDoctorsList(state.getHospitalId(), directionId);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        assert array != null;
        if (array.length() > 100) {
            logger.warning("records more than 100: " + array.length()); //смотрю чтоб не было длиннее 100 записей
        }
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsObj = array.getJSONObject(i);
            String doctorName = jsObj.get("name").toString();
            String tickets = ": " + jsObj.get("freeTicketCount");
            logger.info(doctorName);
            if ((doctorName).getBytes().length > 64 - tickets.getBytes().length) {
                doctorName = shortDoctorName(doctorName, 64 - tickets.getBytes().length);
                logger.warning("LONGER THAN 64 BYTES -> " + doctorName);
            }
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(doctorName + tickets);  //отправляем сокращенное имя и колво талонов
            inlineKeyboardButton.setCallbackData(jsObj.get("id").toString());                                 //получаем id
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        }

        //добавить кнопку без разницы какой доктор (отслеживать только направление)
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(ALL_DOCTORS);
        inlineKeyboardButton.setCallbackData("-1");
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);

        //назад
        rowsInline.add(addBackButton());
        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public ReplyKeyboard submitButton(Long chatId, String doctorId) {
        stateService.saveDoctor(chatId, doctorId);
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("Подтвердить");
        inlineKeyboardButton.setCallbackData(TRACKING.getHospitalCommandName());
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);

        //назад
        rowsInline.add(addBackButton());
        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public List<InlineKeyboardButton> addBackButton() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(BACK);
        inlineKeyboardButton.setCallbackData(BACK);
        rowInline.add(inlineKeyboardButton);
        return  rowInline;
    }
}

