package com.alexey.MessageSender;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Component
public class Sender extends DefaultAbsSender {

    @Value("${bot.token}")
    private String botToken;
    public Sender() {
        super(new DefaultBotOptions());
        // Из .env файла
    }
    @Override
    public String getBotToken() {
        return botToken;
    }



    // Убираем ограничение extends Serializable
    public void send(SendMessage message) throws TelegramApiException {
        System.out.println(message.getChatId()+message.getText());
        execute(message);
    }

    public void edit(EditMessageText message) throws TelegramApiException {

        System.out.println(message.getChatId()+message.getText());
        execute(message);
    }
    public void sendAudio(SendAudio audio) throws TelegramApiException {
        execute(audio);
    }
    public void sendFile(SendDocument file) throws TelegramApiException {
        execute(file);
    }
}
