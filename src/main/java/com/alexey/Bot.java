package com.alexey;

import com.alexey.commands.DeleteWord;
import com.alexey.commands.SaveWord;
import com.alexey.models.Word;
import com.alexey.repository.WordRepository;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.springframework.stereotype.Component;
import io.github.cdimascio.dotenv.Dotenv;


@Component
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private  WordService wordService;
    @Autowired
    private SaveWord saveWord;
    @Autowired
    private DeleteWord deleteWord;
    Dotenv dotenv = Dotenv.load();


    @Override
    public String getBotUsername() {
        return "LanguageSlayer";
    }
    @Override
    public String getBotToken() {
        return dotenv.get("BOT_TOKEN");
    }
    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()){
            String message = update.getMessage().getText();
            long ChatId = update.getMessage().getChatId();
            SendMessage sendMessage = new SendMessage();

            if(message.startsWith("/save")) {
                try {
                    SendMessage response = saveWord.saveWord(message, ChatId);
                    execute(response); // ОТПРАВЛЯЕТ

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // Действие при команде /delete
            if(message.startsWith("/delete")) {
                try {
                    SendMessage response = deleteWord.DeleteWord(message,ChatId);
                    execute(response);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }


            }

        }

    }



