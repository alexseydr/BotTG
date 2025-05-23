package com.alexey.MessageSender;

import com.alexey.repository.WordRepository;
import com.alexey.service.MakeSound;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.*;

@Service
public class ServiceSend {

    private final Sender sender;
    private final Schedule schedule;
    private final MakeSound makeSound;
    private WordRepository wordRepository;
    private WordService wordService;
    @Autowired
    private MakeSound makeSoundService;

    public ServiceSend(Sender sender, Schedule schedule, WordRepository wordRepository, WordService wordService, MakeSound makeSound) {
        this.sender = sender;
        this.schedule = schedule;
        this.wordRepository = wordRepository;
        this.wordService = wordService;
        this.makeSound = makeSound;
    }

    public void getCallbackResponse(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String originalText = callbackQuery.getMessage().getText(); // сохраняем оригинальный текст
        String responseSuffix;

        InlineKeyboardMarkup markup = null;

        if ("Correct answer".equals(callbackData)) {
            responseSuffix = "\n\n✅ Правильно! Отличная работа.";
        } else if (callbackData.startsWith("Incorrect answer")) {
            responseSuffix = "\n\n❌ Неправильно. Попробуй ещё!";
            // оставляем клавиатуру
            markup = (InlineKeyboardMarkup) callbackQuery.getMessage().getReplyMarkup();
        } else {
            return;
        }

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        editMessage.setText(originalText + responseSuffix);
        editMessage.setParseMode("Markdown");

        if (markup != null) {
            editMessage.setReplyMarkup(markup);
        }

        try {
            sender.edit(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Scheduled(cron = "0 01 13 * * *")
    public void sendDailyMessage() {
        Map<Long, List<String>> messages = schedule.MessagingQueue();
        for (Map.Entry<Long, List<String>> entry : messages.entrySet()) {
            Long userId = entry.getKey();
            List<String> userMessage = entry.getValue();
            List<String> allTranslations = wordRepository.getTranslationsByUserId(userId);

            for (int i = 0; i < userMessage.size(); i++) {
                List<String> AllIncorrectTranslation = new ArrayList<>(allTranslations); // копия
                Collections.shuffle(AllIncorrectTranslation);
                String correctAnswer = wordRepository.getTranslationByUserIdAndWord(userId, userMessage.get(i));
                try{
                    AllIncorrectTranslation.remove(correctAnswer);
                }
                catch (Exception e){

                }
                List<String> SelectedIncorrectTranslation = AllIncorrectTranslation.subList(0, 3);
                SelectedIncorrectTranslation.add(correctAnswer);
                Collections.shuffle(SelectedIncorrectTranslation);
                List<InlineKeyboardButton> buttons = new ArrayList<>();
                for (String Answer : SelectedIncorrectTranslation) {
                    InlineKeyboardButton button = new InlineKeyboardButton(Answer);
                    if (Answer.equals(correctAnswer)) {
                        button.setCallbackData("Correct answer");
                    } else {
                        button.setCallbackData("Incorrect answer " + UUID.randomUUID());
                    }
                    buttons.add(button);
                }

                List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                for (InlineKeyboardButton button : buttons) {
                    keyboard.add(Collections.singletonList(button));
                }
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                markup.setKeyboard(keyboard);

                SendMessage message = new SendMessage();
                message.setChatId(userId.toString());
                message.setText("Привет, выбери правильный перевод слова: \n" + userMessage.get(i));
                SendAudio sendAudio = new SendAudio();
                sendAudio.setChatId(userId.toString());
                InputStream stream = makeSound.makeSound(userMessage.get(i));
                sendAudio.setAudio(new InputFile(stream, userMessage.get(i) + ".mp3"));

                try {
                    sender.sendAudio(sendAudio);
                    message.setReplyMarkup(markup);
                    sender.send(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}