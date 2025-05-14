package com.alexey.MessageSender;
import com.alexey.repository.WordRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Schedule {
    private final WordRepository wordRepository;

    public Schedule(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Scheduled(cron = "0 30 6 * * *") // выполняется каждый день в 6:00

    public void Scheduling() {
        wordRepository.updateAllDelayBetween(); // Обновление значений delayBetween
        Map<Long, List<String>> messages = MessagingQueue(); // Получаем сообщения для пользователей

    }

    public Map<Long, List<String>> MessagingQueue() {
        List<Long> userIds = wordRepository.getAllUserId(); // Получаем всех пользователей
        Map<Long, List<String>> messageMap = new HashMap<>();

        for (Long userId : userIds) {
            // Получаем слова и переводы для каждого пользователя
            List<String> words = wordRepository.getWordsByUserIdAndDelayBetween(String.valueOf(userId));
            messageMap.put(userId,words);
        }

        return messageMap;
    }
}


