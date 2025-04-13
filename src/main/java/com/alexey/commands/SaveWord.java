package com.alexey.commands;

import com.alexey.models.Word;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.alexey.Bot;

@Component
public class SaveWord {
    @Autowired
    private WordService wordService;
    private Bot bot;

    public SendMessage saveWord(String message, Long ChatId ) throws TelegramApiException {

        String[] words = message.split(" ", 3);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(ChatId);
        if(words.length == 3) { // Идет проверка, что клиент разделил на 3 части запрос

            wordService.SaveWord(new Word(null, words[1], words[2])); // айди(авто, слово, перевод)
            sendMessage.setChatId(ChatId); // Определяет айди чата
            sendMessage.setText(words[1]); // Устанавливает текст в чате
            return sendMessage;

        }
        else{ // Клиент не разделил на 3 части запрос

            sendMessage.setChatId(ChatId);
            sendMessage.setText("Введите команду по примеру: Команда слово перевод :: /save dog собака");
            return sendMessage;

        }


    }
}