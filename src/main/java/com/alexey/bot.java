package com.alexey;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class bot extends TelegramLongPollingBot {
@Override
    public String getBotUsername() {
    return "LanguageSlayer";
}
@Override
public String getBotToken() {
    return "7817501171:AAGlyI9IoTc4Ht7UDmU6tzF7eb8C3JsnGIM";
}
@Override
    public void onUpdateReceived(Update update) {
    if(update.hasMessage() && update.getMessage().hasText()){
        String message = update.getMessage().getText();
        System.out.println(message);
    }
}
public static void main(String[] args) {
    try{
        TelegramBotsApi bot = new TelegramBotsApi(DefaultBotSession.class);
        bot.registerBot(new bot());
    }
    catch (TelegramApiException e){
        e.printStackTrace();
    }
}

}
