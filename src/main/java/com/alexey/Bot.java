package com.alexey;

import com.alexey.MessageSender.Schedule;
import com.alexey.MessageSender.Sender;
import com.alexey.MessageSender.ServiceSend;
import com.alexey.commands.DeleteWord;
import com.alexey.commands.ListWord;
import com.alexey.commands.SaveWord;
import com.alexey.models.Word;
import com.alexey.repository.WordRepository;
import com.alexey.service.MakeSound;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;



@Component
public class Bot extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.username}")
    private String botUsername;
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
    @Autowired
            private Sender sender;
    @Autowired
        private Schedule schedule;
    @Autowired
    private MakeSound makeSound;



    @Autowired
    private WordService wordService;


    @Override
    public String getBotUsername() {
        return botUsername;
    }
    @Override
    public String getBotToken() {
        return botToken;
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
                    "❗❗ Краткость — сестра таланта! Лучше избегать длинных фраз или предложений, так как они могу плохо отображаться. Например, предложение \"The sky turned dark and heavy, but light still found a way in.\" является максимальным по количеству символов (62 символа, включая все знаки препинания, пробелы.  Так обучение пройдёт без сбоев.\n" +
                    "\n" +
                    "\uD83D\uDCCC Основные команды:\n" +
                    "\n" +
                    "/save \"слово\" \"перевод\" — добавляет новое слово в твой личный словарь.  \n" +
                    "/delete \"слово\" \"перевод\" — удаляет слово из твоего словаря.\n" +
                        "/list - показывает ваш словарь.\n");
            try {

                execute(startMessage);
                wordService.SaveWord(new Word(null, "dog", "собака", LocalDate.now().minusDays(31), null, String.valueOf(ChatId)));
                wordService.SaveWord(new Word(null, "journey", "путешествие", LocalDate.now().minusDays(31), null, String.valueOf(ChatId)));
                wordService.SaveWord(new Word(null, "memory", "память", LocalDate.now().minusDays(31), null, String.valueOf(ChatId)));
                wordService.SaveWord(new Word(null, "decision", "решение", LocalDate.now().minusDays(31), null, String.valueOf(ChatId)));
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
            }

            if(message.startsWith("/delete")) {
                try {
                    SendMessage response = deleteWord.DeleteWord(message,ChatId);
                    execute(response);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            if (message.startsWith("/testButtons")) {
                try {
                    wordRepository.updateAllDelayBetween(); // пусть остаётся, если нужно

                    List<String> options = Arrays.asList("The sky turned dark and heavy, but light still found a way in.", "Ответ 2", "Ответ 3", "Ответ 4");
                    String correctAnswer = "Ответ 2";

                    List<InlineKeyboardButton> buttons = new ArrayList<>();
                    for (String option : options) {
                        InlineKeyboardButton button = new InlineKeyboardButton(option);
                        if (option.equals(correctAnswer)) {
                            button.setCallbackData("Correct answer");
                        } else {
                            button.setCallbackData("Incorrect answer " + UUID.randomUUID());
                        }
                        buttons.add(button);
                    }

                    List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                    for (InlineKeyboardButton button : buttons) {
                        keyboard.add(Collections.singletonList(button)); // каждая кнопка в отдельной строке
                    }

                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    markup.setKeyboard(keyboard);
                    SendAudio sendAudio = new SendAudio();
                    InputStream stream = makeSound.makeSound("The sky turned dark and heavy, but light still found a way in.");
                    sendAudio.setChatId(ChatId);
                    sendAudio.setAudio(new InputFile(stream, "sound.mp3"));
                    execute(sendAudio);

                    SendMessage sendMessage1 = new SendMessage();
                    sendMessage1.setChatId(ChatId);
                    sendMessage1.setText("Это тест. Выбери правильный ответ:");
                    sendMessage1.setReplyMarkup(markup);

                    execute(sendMessage1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(message.startsWith("/testUpdate")){
                try{
                    wordRepository.updateAllDelayBetween();
                }
                catch (Exception e){}
            }

            if (message.startsWith("/list")) {

                try {
                    List<SendMessage> messages = listWord.listWords(String.valueOf(ChatId));
                    for (SendMessage msg : messages) {
                        execute(msg);
                    }
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }



        }

        }

    }



