package com.alexey.commands;
import com.alexey.repository.WordRepository;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


import java.util.List;


@Component
public class ListWord {
    @Autowired
    private WordService wordService;
    @Autowired
    private WordRepository wordRepository;

    public SendMessage ListWords(String chatId)throws TelegramApiException {
        List<Object[]> listWords = wordRepository.getWordsAndTranslationsByUserId(String.valueOf(chatId));
        StringBuilder listmsg = new StringBuilder();
        SendMessage listMessage = new SendMessage();
        listmsg.append("Вот твой словарь на данный момент:\n\n");
        for (Object[] entry : listWords) {
            String word = (String) entry[0]; // слово
            String translation = (String) entry[1]; // перевод
            listmsg.append(word).append(" - ").append(translation).append("\n");
        }
        listMessage.setChatId(chatId);
        listMessage.setText(listmsg.toString());
        return listMessage;

    }

}
