package com.alexey.MessageSender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class ServiceSend {

    private final Sender sender;

    @Value("7685744536")  // Получаем chatId из application.properties или .env
    private String chatId;

    public ServiceSend(Sender sender) {
        this.sender = sender;
    }

    // Планируемое отправление сообщения каждый день в 10:00
    @Scheduled(cron = "*/10 * * * * *")
    public void sendDailyMessage() {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Доброе утро! Время повторить слова!");

        try {
            sender.send(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
