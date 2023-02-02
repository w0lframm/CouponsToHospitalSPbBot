package com.example.couponstohospitalbot.telegram.keyboards;

import static com.example.couponstohospitalbot.telegram.command.CommandName.*;

public class Constants {

    public static final String ALL_DOCTORS = "Без разницы";
    public static final String BACK = "Назад";
    public static final String CHOOSE_HOSPITAL = ". Выберите больницу:";
    public static final String CHOOSE_MESSAGE = ". Выберите район:";
    public static final String CHOOSE_DIRECTION = ". Выберите направление:";
    public static final String CHOOSE_DOCTOR = ". Выберите доктора:";
    public static final String CONFIRM_MESSAGE = "Подтвердите правильность введенных данных, или измените значения: \n";
    public static final String WAIT_MESSAGE = "\nЯ пришлю уведомление, как только появятся нужные вам талончики," +
            " ожидайте :) Если не хотите, чтобы я вас сильно тревожил, отключите в настройках звук на уведомления" +
            " (они будут приходить, пока вы не нажмете /stop_alarm)";
    public static final String ANSWER_MESSAGE = "Ура! Появились нужные вам талончики, скорее записывайтесь!\n";
    public static final String EMPTY_COLLECTION = "Коллекция пуста! (Вероятно все остальные запросы еще отслеживаются)";

    public static final String UNKNOWN_MESSAGE = "Не понимаю вас \uD83D\uDE1F, напишите /help чтобы узнать что я понимаю.";
    public static final String STOP_MESSAGE = "Деактивировал все ваши подписки \uD83D\uDE1F.";
    public final static String START_MESSAGE = "Привет. Я помогу тебе узнать о новых талончиках в твою поликлиничечку. " +
            "Но я еще маленький и только учусь.\n" +
            "Если хочешь выбрать новую поликлинику нажми /choose";

    public static final String NO_MESSAGE = "Я поддерживаю команды, начинающиеся со слеша(/).\n"
            + "Чтобы посмотреть список команд введите /help";
    public static final String STOP_ALARM = "Чтобы остановить оповещения, нажми /stop_alarm";
    public static final String ONLY_PHONE_MESSAGE = "В эту поликлинику можно записаться только по телефону";

    public static final String HELP_MESSAGE = String.format("✨ <b> Доступные команды </b> ✨\n\n"
                    + "<b> Начать\\закончить работу с ботом </b>\n"
                    + "%s - начать работу со мной\n"
                    + "%s - приостановить работу со мной\n"
                    + "%s - начать выбор для отслеживания\n"
                    + "%s - посмотреть коллекцию посещений\n"
                    + "%s - получить помощь в работе со мной",
            START.getCommandName(), STOP.getCommandName(), CHOOSE.getCommandName(), COLLECTION.getCommandName(), HELP.getCommandName());
}
