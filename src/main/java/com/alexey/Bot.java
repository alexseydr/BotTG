package com.alexey;

import com.alexey.models.Word;
import com.alexey.repository.WordRepository;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.springframework.stereotype.Component;

@Component
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private  WordService wordService;



    @Override
    public String getBotUsername() {
        return "LanguageSlayer";
    }
    @Override
    public String getBotToken() {
        return "7817501171:AAGlyI9IoTc4Ht7UDmU6tzF7eb8C3JsnGIM";
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String message = update.getMessage().getText();
            long ChatId = update.getMessage().getChatId();
            SendMessage sendMessage = new SendMessage();

            if(message.startsWith("/save")){

                String[] words = message.split(" ",3); // Деление команды на 3 составные части
                if(words.length == 3) { // Идет проверка, что клиент разделил на 3 части запрос

                    wordService.SaveWord(new Word(null, words[1], words[2])); // айди(авто, слово, перевод)
                    sendMessage.setChatId(ChatId); // Определяет айди чата
                    sendMessage.setText(words[1]); // Устанавливает текст в чате
                    try {
                        execute(sendMessage); // ОТПРАВЛЯЕТ
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{ // Клиент не разделил на 3 части запрос
                    SendMessage sendMessage1 = new SendMessage();
                    sendMessage1.setChatId(ChatId);
                    sendMessage1.setText("Введите команду по примеру: Команда слово перевод :: /save dog собака");
                    try {
                        execute(sendMessage1); // ОТПРАВЛЯЕТ
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }


            }

        }

    }


}
