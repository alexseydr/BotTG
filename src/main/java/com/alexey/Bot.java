package com.alexey;

import com.alexey.MessageSender.Sender;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.stereotype.Component;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDate;
import java.util.*;


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
    @Autowired
            private Sender sender;

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
                    "❗❗ Краткость — сестра таланта! Лучше избегать длинных фраз или предложений, так как они могу плохо отображаться. Например, предложение \"The sky turned dark and heavy, but light still found a way in.\" является максимальным по количеству символов (62 символа, включая все знаки препинания, пробелы).  Так обучение пройдёт без сбоев.\n" +
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
            } // Действие при команде /delete
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
                    SendMessage sendMessage1 = new SendMessage();
                    sendMessage1.setChatId(String.valueOf(7685744536L));
                    sendMessage1.setText("Это тест. Выбери правильный ответ:");
                    sendMessage1.setReplyMarkup(markup);

                    sender.send(sendMessage1);
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
                    SendMessage responce = listWord.ListWords(String.valueOf(ChatId));
                    execute(responce);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }



        }

        }

    }



