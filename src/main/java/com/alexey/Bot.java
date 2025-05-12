package com.alexey;

import com.alexey.MessageSender.ServiceSend;
import com.alexey.commands.DeleteWord;
import com.alexey.commands.SaveWord;
import com.alexey.repository.WordRepository;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.stereotype.Component;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;


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
    @Autowired
            private ServiceSend serviceSend;
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
        if (update.hasCallbackQuery()) {
            serviceSend.getCallbackResponse(update.getCallbackQuery());
        }
        if(update.hasMessage() && update.getMessage().hasText()){
            String message = update.getMessage().getText();
            long ChatId = update.getMessage().getChatId();
            String UserId = update.getMessage().getChatId().toString();
            SendMessage sendMessage = new SendMessage();

            if(message.startsWith("/save")) {// Действие при команде /save

                try {

                    SendMessage response = saveWord.saveWord(message, ChatId,UserId);
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
            if(message.startsWith("/test")){
                try{
                    wordRepository.updateAllDelayBetween();
                }
                catch (Exception e){}
            }


            }

        }

    }



