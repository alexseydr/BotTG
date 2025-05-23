package com.alexey.commands;
import com.alexey.repository.WordRepository;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


import java.util.ArrayList;
import java.util.List;


@Component
public class ListWord {
    @Autowired
    private WordService wordService;
    @Autowired
    private WordRepository wordRepository;

    public List<SendMessage> listWords(String chatId) {
        List<Object[]> listWords = wordRepository.getWordsAndTranslationsByUserId(chatId);
        List<SendMessage> messages = new ArrayList<>();
        StringBuilder currentMessage = new StringBuilder("Вот твой словарь на данный момент:\n\n");

        for (Object[] entry : listWords) {
            String word = (String) entry[0];
            String translation = (String) entry[1];
            String line = word + " - " + translation + "\n";

            if (currentMessage.length() + line.length() > 4000) {
                // отправляем накопленное
                SendMessage part = new SendMessage();
                part.setChatId(chatId);
                part.setText(currentMessage.toString());
                messages.add(part);

                // начинаем новое сообщение
                currentMessage = new StringBuilder();
            }

            currentMessage.append(line);
        }

        // добавляем остаток
        if (currentMessage.length() > 0) {
            SendMessage part = new SendMessage();
            part.setChatId(chatId);
            part.setText(currentMessage.toString());
            messages.add(part);
        }

        return messages;
    }


}
