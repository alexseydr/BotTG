package com.alexey;

import com.alexey.MessageSender.ServiceSend;
import com.alexey.commands.DeleteWord;
import com.alexey.commands.ListWord;
import com.alexey.commands.SaveWord;
import com.alexey.models.Word;
import com.alexey.repository.WordRepository;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.stereotype.Component;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDate;


@Component
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private SaveWord saveWord;
    @Autowired
    private DeleteWord deleteWord;
    @Autowired
            private ServiceSend serviceSend;
    @Autowired
            private ListWord listWord;

    Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\amals\\IdeaProjects\\BotTG1").load();
    @Autowired
    private WordService wordService;


    @Override
    public String getBotUsername() {
        return "LanguageSlayer";
    }
    @Override
    public String getBotToken() {
        return dotenv.get("BOT_TOKEN");
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            serviceSend.getCallbackResponse(update.getCallbackQuery());
        }
        if(update.hasMessage() && update.getMessage().hasText()){

            String message = update.getMessage().getText();
            long ChatId = update.getMessage().getChatId();
            String UserId = update.getMessage().getChatId().toString();
            SendMessage sendMessage = new SendMessage();

            if(message.startsWith("/start")){
            SendMessage startMessage = new SendMessage();
            startMessage.setChatId(ChatId);
                startMessage.setText("Привет! \uD83D\uDC4B\n" +
                    "\n" +
                    "Рад приветствовать тебя в нашем учебном боте по изучению иностранных слов! ✨\n" +
                    "\n" +
                    "Перед началом занятий ознакомься с простыми правилами:\n" +
                    "\n" +
                    "✅ Краткость — сестра таланта! Лучше избегать длинных предложений, так как они могут обрезаться. Например, фразу \"Hello, my name is...\" рекомендуем сократить до простого \"Hello\". Так обучение пройдёт без сбоев.\n" +
                    "\n" +
                    "\uD83D\uDCCC Основные команды:\n" +
                    "\n" +
                    "/save \"слово\" \"перевод\" — добавляет новое слово в твой личный словарь.  \n" +
                    "/delete \"слово\" \"перевод\" — удаляет слово из твоего словаря.\n" +
                        "/list - показывает ваш словарь.\n");
            try {
                execute(startMessage);
                wordService.SaveWord(new Word(null, "dog", "собака", LocalDate.now(), null, String.valueOf(ChatId)));
                wordService.SaveWord(new Word(null, "journey", "путешествие", LocalDate.now(), null, String.valueOf(ChatId)));
                wordService.SaveWord(new Word(null, "memory", "память", LocalDate.now(), null, String.valueOf(ChatId)));
                wordService.SaveWord(new Word(null, "decision", "решение", LocalDate.now(), null, String.valueOf(ChatId)));
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
            }
            if(message.startsWith("/save")) {// Действие при команде /save

                try {

                    SendMessage response = saveWord.saveWord(message, ChatId,UserId);
                    execute(response); // ОТПРАВЛЯЕТ

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // Действие при команде /delete
            if(message.startsWith("/delete")) {
                try {
                    SendMessage response = deleteWord.DeleteWord(message,ChatId);
                    execute(response);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            if(message.startsWith("/test012345")){
                try{
                    wordRepository.updateAllDelayBetween();
                }
                catch (Exception e){}
            }
            if (message.startsWith("/list")) {

                try {
                    SendMessage responce = listWord.ListWords(String.valueOf(ChatId));
                    execute(responce);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }



        }

        }

    }



