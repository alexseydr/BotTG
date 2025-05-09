package com.alexey.MessageSender;

import com.alexey.repository.WordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.alexey.MessageSender.Schedule;

import java.util.List;
import java.util.Map;

@Service
public class ServiceSend {

    private final Sender sender;
    private final Schedule schedule; // Инжектируем Schedule
    private WordRepository wordRepository;

    public ServiceSend(Sender sender, Schedule schedule, WordRepository wordRepository) {
        this.sender = sender;
        this.schedule = schedule;
        this.wordRepository = wordRepository;
    }


    @Scheduled(cron = "*/10 * * * * *")
    public void sendDailyMessage() {
        Map<Long, String> messages = schedule.MessagingQueue(); // Вызов MessagingQueue() из Schedule

        // Проходим по всем пользователям и отправляем сообщения
        for (Map.Entry<Long, String> entry : messages.entrySet()) {
            Long userId = entry.getKey(); // userId
            String userMessage = entry.getValue(); // Сообщение для пользователя

            SendMessage message = new SendMessage();
            message.setChatId(userId.toString()); //
            message.setText("Привет, вот твои слова: \n" + userMessage);

            try {
                sender.send(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}



