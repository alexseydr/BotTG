package com.alexey.commands;

import com.alexey.models.word;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.bots.AbsSender;
import com.alexey.bot;

import com.alexey.service.WordService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SaveWord implements commandGeneral {
    private final WordService wordService;
    private final AbsSender bot;

    public SaveWord(WordService wordService, AbsSender bot) {
        this.wordService = wordService;
        this.bot = bot;
    }
    @Override
    public String getCommand() {
        return "/save";
    }
    @Override
    public void execute (Update update) {
        String message = update.getMessage().getText();
        String[] words = message.split(" ",3);

        if(words.length == 3) {
            String word = words[1];
            String translation = words[2];
            wordService.SaveWord(new word(null, word, translation));
            SendMessage msg = new SendMessage();
            msg.setChatId(update.getMessage().getChatId());
            msg.setText("Слово: "+word+ "+ и перевод:" +translation + " сохранены");
            try{

                bot.execute(msg);  // отправляем сообщение
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }



}
