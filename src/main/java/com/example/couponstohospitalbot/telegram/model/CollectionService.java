package com.example.couponstohospitalbot.telegram.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.EMPTY_COLLECTION;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CollectionService {
    private final TrackingRepository trackingRepository;
    private final TrackingService trackingService;
    public ReplyKeyboard getCollection(Long chatId) throws IOException, URISyntaxException {
        List<Tracking> listTrack = trackingRepository.listFinishedTrackIdByChatId(chatId);
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        if (listTrack != null) {
            int index = 1;
            for (var id : listTrack) {
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(index + ".");
                inlineKeyboardButton.setCallbackData("/col_trackId " + id.getTrackId());
                rowInline.add(inlineKeyboardButton);
                rowsInline.add(rowInline);
                index++;
            }
        }
        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public ReplyKeyboard getChoosingButtons(Long trackId) { //отследить, удалить, назад
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("Отследить");
        inlineKeyboardButton.setCallbackData("/col_submit " + trackId + " отследить");
        rowInline.add(inlineKeyboardButton);

        inlineKeyboardButton = new InlineKeyboardButton("Удалить");
        inlineKeyboardButton.setCallbackData("/col_submit " + trackId + " удалить");
        rowInline.add(inlineKeyboardButton);

        rowsInline.add(rowInline);

        rowInline = new ArrayList<>();
        inlineKeyboardButton = new InlineKeyboardButton("Назад");
        inlineKeyboardButton.setCallbackData("/col_back ");
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public ReplyKeyboard getSubmitButtons(Long trackId, String action) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("Да");
        inlineKeyboardButton.setCallbackData(action + trackId);
        rowInline.add(inlineKeyboardButton);

        inlineKeyboardButton = new InlineKeyboardButton("Нет");
        inlineKeyboardButton.setCallbackData("/col_back ");
        rowInline.add(inlineKeyboardButton);
        rowsInline.add(rowInline);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public String getVisitInfo(Long chatId) throws IOException, URISyntaxException {
        List<Tracking> listTrackId = trackingRepository.listFinishedTrackIdByChatId(chatId);
        if (listTrackId != null) {
            int index = 1;
            StringBuilder sb = new StringBuilder();
            for (var id : listTrackId) {
                sb.append(index).append(". ").append(trackingService.getRequestInfo(id.getTrackId())).append('\n');
                index++;
            }
            return sb.toString();
        }
        return EMPTY_COLLECTION;
    }

    public void deleteItem(Long trackId) {
        Optional<Tracking> tracking = trackingRepository.findById(trackId);
        if (tracking.isPresent()) {
            Tracking track = tracking.get();
            track.setIsDeleted(true);
            trackingRepository.save(track);
        }
    }

    public void trackItem(Long trackId) {
        Optional<Tracking> tracking = trackingRepository.findById(trackId);
        if (tracking.isPresent()) {
            Tracking track = tracking.get();
            track.setIsFinished(false);
            trackingRepository.save(track);
        }
        trackingService.addEvent(trackId);
    }
}
