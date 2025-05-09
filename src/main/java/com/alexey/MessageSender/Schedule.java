package com.alexey.MessageSender;
import com.alexey.models.Word;
import com.alexey.repository.WordRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Schedule {
    private final WordRepository wordRepository;

    public Schedule(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    //@Scheduled(cron = "0 0 6 * * *") // выполняется каждый день в 6:00
    @Scheduled(cron = "*/10 * * * * *")
    public void Scheduling() {
        wordRepository.updateAllDelayBetween(); // Обновление значений delayBetween
        Map<Long, String> messages = MessagingQueue(); // Получаем сообщения для пользователей

    }

    public Map<Long, String> MessagingQueue() {
        List<Long> userIds = wordRepository.getAllUserId(); // Получаем всех пользователей
        Map<Long, String> messageMap = new HashMap<>();

        for (Long userId : userIds) {
            // Получаем слова и переводы для каждого пользователя
            List<String> words = wordRepository.getWordsByUserIdAndDelayBetween(String.valueOf(userId));
            List<String> translations = wordRepository.getTranslationsByUserId(String.valueOf(userId));

            StringBuilder message = new StringBuilder();
            for (int i = 0; i < words.size(); i++) {
                message.append(words.get(i)).append(" - ").append(translations.get(i)).append("\n");
            }

            messageMap.put(userId, message.toString());
        }

        return messageMap;
    }
}


