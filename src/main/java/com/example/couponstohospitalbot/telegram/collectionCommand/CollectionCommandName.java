package com.example.couponstohospitalbot.telegram.collectionCommand;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CollectionCommandName {
    BACK("/col_back "),
    CHOOSE_ITEM("/col_trackId "),
    SUBMIT("/col_submit "),
    DELETE_ITEM("/col_delete "),
    TRACKING_ITEM("/col_tracking ");

    private final String commandName;
}
