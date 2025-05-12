package com.alexey.commands;

import com.alexey.models.Word;
import com.alexey.repository.WordRepository;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.alexey.Bot;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SaveWord {
    @Autowired
    private WordService wordService;
    private Bot bot;
    @Autowired
    private WordRepository wordRepository;

    public SendMessage saveWord(String message, Long chatId, String userId) throws TelegramApiException {

        // Регулярное выражение для поиска строк в кавычках
        Pattern pattern = Pattern.compile("\"([^\"]+)\""); // Это ищет текст в кавычках
        Matcher matcher = pattern.matcher(message);

        SendMessage sendMessage = new SendMessage();
        LocalDate date = LocalDate.now();

        // Проверяем, что в сообщении есть две строки в кавычках
        if (matcher.find()) {
            String wordText = matcher.group(1);  // Первая строка в кавычках - слово
            if (matcher.find()) {
                String translationText = matcher.group(1);  // Вторая строка в кавычках - перевод

                // Проверяем, существует ли такая пара слово-перевод в базе
                if (wordRepository.existsByWordAndTranslation(wordText, translationText)) {
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Такая пара слово-перевод уже существует");
                    return sendMessage;
                } else {
                    // Сохраняем новое слово с переводом
                    wordService.SaveWord(new Word(null, wordText, translationText, date, null, userId));
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Пара \"" + wordText + "\" - \"" + translationText + "\" сохранена в словарь!");
                    return sendMessage;
                }
            }
        }

        // Если не найдены две строки в кавычках
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите команду по примеру: /save \"слово\" \"перевод\"");
        return sendMessage;
    }
}
