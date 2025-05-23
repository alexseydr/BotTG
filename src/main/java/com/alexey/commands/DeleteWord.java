package com.alexey.commands;

import com.alexey.repository.WordRepository;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.alexey.Bot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DeleteWord {
    @Autowired
    private WordService wordService;
    @Autowired
    private WordRepository wordRepository;
    private Bot bot;

    public SendMessage DeleteWord(String message, Long chatId) throws TelegramApiException {
        Pattern pattern = Pattern.compile("[\"“”]([^\"“”]+)[\"“”]");
        Matcher matcher = pattern.matcher(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (matcher.find()) {
            String wordText = matcher.group(1).trim();
            if (matcher.find()) {
                String translationText = matcher.group(1).trim();


                if (wordText.isEmpty() || translationText.isEmpty()) {
                    sendMessage.setText("Слово и перевод не могут быть пустыми.");
                    return sendMessage;
                }


                if (!wordText.matches("[a-zA-Zа-яА-ЯёЁ\\s()]+") || !translationText.matches("[a-zA-Zа-яА-ЯёЁ\\s()]+")) {
                    sendMessage.setText("Слово и перевод могут содержать только буквы и пробелы.");
                    return sendMessage;
                }


                if (wordRepository.existsByWordAndTranslationAndUserId(wordText, translationText, String.valueOf(chatId))) {
                    wordService.DeleteWord(wordText, translationText, String.valueOf(chatId));
                    sendMessage.setText("Пара: \"" + wordText + "\" - \"" + translationText + "\" удалена!");
                    return sendMessage;
                } else {
                    sendMessage.setText("Такой пары слово-перевод не существует. Возможно, она уже была удалена.");
                    return sendMessage;
                }
            }
        }

        sendMessage.setText("Введите команду по примеру: /delete \"слово\" \"перевод\"");
        return sendMessage;
    }
}
