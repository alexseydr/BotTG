package com.alexey.MessageSender;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.alexey.MessageSender.Schedule;

@Component
public class Sender extends DefaultAbsSender {

    private final String botToken;

    public Sender() {
        super(new DefaultBotOptions());
        this.botToken = Dotenv.load().get("BOT_TOKEN");  // Из .env файла
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    // Метод для отправки сообщений
    public void send(SendMessage message) throws TelegramApiException {
        execute(message);
    }
}
