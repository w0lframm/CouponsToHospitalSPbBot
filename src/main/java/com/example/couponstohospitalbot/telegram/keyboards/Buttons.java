package com.example.couponstohospitalbot.telegram.keyboards;

import org.json.JSONArray;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Buttons {

    private static String districtId = null;
    private static int hospitalId = -1;

    private static String directionId = null;

    private static String doctorId = null;

    public static SendMessage setButtonsDistrict(Long chatId) throws URISyntaxException, IOException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите район");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        JSONArray nameDistrict = ParsingJson.getRegionsList();
        // кнопки вместо клавы
        List<KeyboardRow> keyboardRows = new ArrayList<>();             // лист рядов кнопок

        int i = 0;
        while(i < nameDistrict.length()) {
            KeyboardRow row = new KeyboardRow();
            row.add(nameDistrict.getJSONObject(i).get("name").toString());                                            // добавляем кнопки в ряд
            keyboardRows.add(row); // добавляем ряд в лист
            i = i+1;
        }

        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);  // устанавливаем кнопки вместо клав
        message.setReplyMarkup(keyboardMarkup);                         // к исходящему от нас сообщению добавляем клаву
        return message;
    }


    public static SendMessage setButtonsHospital(Long chatId, String districtName) throws URISyntaxException, IOException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите больницу");
        districtId = ParsingJson.findRegionIdByName(districtName);
        JSONArray nameHospital = ParsingJson.getHospitalList(districtId);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(); // кнопки вместо клавы
        List<KeyboardRow> keyboardRows = new ArrayList<>();             // лист рядов кнопок

        int i = 0;
        while(i < nameHospital.length()) {
            KeyboardRow row = new KeyboardRow();
            row.add(nameHospital.getJSONObject(i).get("lpuFullName").toString());                                            // добавляем кнопки в ряд
            keyboardRows.add(row); // добавляем ряд в лист
            i = i+1;
        }

        keyboardMarkup.setKeyboard(keyboardRows);// устанавливаем кнопки вместо клавы
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);
        message.setReplyMarkup(keyboardMarkup);                         // к исходящему от нас сообщению добавляем клаву
        return message;
    }

    public static SendMessage setButtonsDirection(Long chatId, String hospitalName) throws URISyntaxException, IOException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите отделение");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        hospitalId = ParsingJson.findHospitalIdByName(districtId, hospitalName);
        JSONArray nameDirection = ParsingJson.getDirectionsList(hospitalId);
        List<KeyboardRow> keyboardRows = new ArrayList<>();             // лист рядов кнопок

        int i = 0;
        while(i < nameDirection.length()) {
            KeyboardRow row = new KeyboardRow();
            row.add(nameDirection.getJSONObject(i).get("name").toString());
            keyboardRows.add(row); // добавляем ряд в лист
            i = i+1;
        }

        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);  // устанавливаем кнопки вместо клав
        message.setReplyMarkup(keyboardMarkup);    // к исходящему от нас сообщению добавляем клаву
        return message;
    }

    public static SendMessage setButtonDoctors(Long chatId, String directionName) throws URISyntaxException, IOException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите врача");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        directionId = ParsingJson.findDirectionIdByName(hospitalId, directionName);
        JSONArray nameDoctors = ParsingJson.getDoctorsList(hospitalId, directionId);
        List<KeyboardRow> keyboardRows = new ArrayList<>();             // лист рядов кнопок

        int i = 0;
        while(i < nameDoctors.length()) {
            KeyboardRow row = new KeyboardRow();
            row.add(nameDoctors.getJSONObject(i).get("name").toString());
            keyboardRows.add(row); // добавляем ряд в лист
            i = i+1;
        }

        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        message.setReplyMarkup(keyboardMarkup);    // к исходящему от нас сообщению добавляем клаву
        return message;
    }

    public static SendMessage setButtonDateAndTime(Long chatId, String doctorsName) throws URISyntaxException, IOException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите талон");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        doctorId = ParsingJson.findDoctorIdByName(hospitalId, directionId, doctorsName);
        JSONArray timetableList = ParsingJson.getTimetableList(hospitalId, doctorId);
        List<KeyboardRow> keyboardRows = new ArrayList<>();             // лист рядов кнопок

        int i = 0;
        while(i < timetableList.length()) {
            KeyboardRow row = new KeyboardRow();
            row.add(timetableList.getJSONObject(i).get("visitStart").toString());
            keyboardRows.add(row); // добавляем ряд в лист
            i = i+1;
        }

        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        message.setReplyMarkup(keyboardMarkup);    // к исходящему от нас сообщению добавляем клаву
        return message;
    }
}
