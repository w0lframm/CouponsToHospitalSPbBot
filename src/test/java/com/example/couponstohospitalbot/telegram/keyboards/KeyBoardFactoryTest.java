package com.example.couponstohospitalbot.telegram.keyboards;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KeyBoardFactoryTest {
    @Autowired
    KeyBoardFactory keyBoardFactory;

    @Test
    void regionButtons() throws IOException, URISyntaxException, JSONException {
        ReplyKeyboard inlineKeyboardToCheck = keyBoardFactory.regionButtons(0L, true);
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        JSONArray array = ParsingJson.getRegionsList();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsObj = array.getJSONObject(i);
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(jsObj.get("name").toString());
            inlineKeyboardButton.setCallbackData(jsObj.get("id").toString());
            rowsInline.add(List.of(inlineKeyboardButton));
        }
        inlineKeyboard.setKeyboard(rowsInline);
        assertEquals(inlineKeyboardToCheck, inlineKeyboard);
    }

}