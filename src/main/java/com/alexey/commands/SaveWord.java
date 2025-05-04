package com.alexey.commands;

import com.alexey.models.Word;
import com.alexey.repository.WordRepository;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import com.alexey.Bot;

import java.time.LocalDate;

@Component
public class SaveWord {
    @Autowired
    private WordService wordService;
    private Bot bot;
    @Autowired
    private WordRepository wordRepository;

    public SendMessage saveWord(String message, Long ChatId, String UserId ) throws TelegramApiException {

        String[] words = message.split(" ", 3);
        SendMessage sendMessage = new SendMessage();
        LocalDate date = LocalDate.now();

        if(wordRepository.existsByWordAndTranslation(words[1], words[2])) { // Если в БД существует уже пара слово-перевод
        sendMessage.setChatId(ChatId);
        sendMessage.setText("Такая пара слово-перевод уже существует");
        return sendMessage;

        }
        else{ // Если в БД не существует пара слово-перевод

            if(words.length == 3) { // Идет проверка, что клиент разделил на 3 части запрос
                wordService.SaveWord(new Word(null, words[1], words[2], date, null,UserId)); // сохранение айди(автоинкремент, слово, перевод)
                sendMessage.setChatId(ChatId); // Определяет айди чата
                sendMessage.setText("Пара "+words[1]+" "+words[2]+" сохранена в словарь!"); // Устанавливает текст в чате
                return sendMessage;

            }
            else{ // Клиент не разделил на 3 части запрос

                sendMessage.setChatId(ChatId);
                sendMessage.setText("Введите команду по примеру: Команда слово перевод :: /save dog собака");
                return sendMessage;

            }
        }




    }
}