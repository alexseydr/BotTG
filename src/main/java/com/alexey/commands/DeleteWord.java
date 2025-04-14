package com.alexey.commands;

import com.alexey.repository.WordRepository;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.alexey.models.Word;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.alexey.Bot;

@Component
public class DeleteWord {
    @Autowired
    private WordService wordService;
    @Autowired
    private WordRepository wordRepository;
    private Bot bot;

public SendMessage DeleteWord(String message, Long ChatId) throws TelegramApiException {

    String[] words = message.split(" ", 3);
    SendMessage sendMessage = new SendMessage();
    String word = words[1];
    String translation = words[2];
    if(wordRepository.existsByWordAndTranslation(word, translation)) {
        if(words.length == 3) { // Идет проверка, что клиент разделил на 3 части запрос

            wordService.DeleteWord(word,translation); // Удаляет слово по конструкции "слово-перевод"
            sendMessage.setChatId(ChatId);
            sendMessage.setText("Пара: "+word+"-"+translation+" удалена!");
            return sendMessage;


        }
        else{ // Клиент не разделил на 3 части запрос

            sendMessage.setChatId(ChatId);
            sendMessage.setText("Введите пару для удаления /delete dog - собака");
            return sendMessage;

        }
    }
    else{
        sendMessage.setChatId(ChatId);
        sendMessage.setText("Такой пары слово-перевод не существует! Возможно, она была удалена ранее");
        return sendMessage;
    }




}

}
