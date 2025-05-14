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


        Pattern pattern = Pattern.compile("\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(message);

        SendMessage sendMessage = new SendMessage();

        // Проверяем, что в сообщении есть две строки в кавычках
        if (matcher.find()) {
            String wordText = matcher.group(1);  // Первая строка в кавычках - слово
            if (matcher.find()) {
                String translationText = matcher.group(1);  // Вторая строка в кавычках - перевод

                // Проверяем, существует ли такая пара слово-перевод в базе
                if (wordRepository.existsByWordAndTranslationAndUserId(wordText, translationText,String.valueOf(chatId))) {
                    // Удаляем пару слово-перевод
                    wordService.DeleteWord(wordText, translationText,String.valueOf(chatId));
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Пара: \"" + wordText + "\" - \"" + translationText + "\" удалена!");
                    return sendMessage;
                } else {
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Такой пары слово-перевод не существует! Возможно, она была удалена ранее.");
                    return sendMessage;
                }
            }
        }

        // Если не найдены две строки в кавычках
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите команду по примеру: /delete \"слово\" \"перевод\"");
        return sendMessage;
    }
}
