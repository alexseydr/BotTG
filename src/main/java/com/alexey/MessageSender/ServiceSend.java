package com.alexey.MessageSender;

import com.alexey.repository.WordRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Service
public class ServiceSend {

    private final Sender sender;
    private final Schedule schedule;
    private WordRepository wordRepository;

    public ServiceSend(Sender sender, Schedule schedule, WordRepository wordRepository) {
        this.sender = sender;
        this.schedule = schedule;
        this.wordRepository = wordRepository;
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


    @Scheduled(cron = "0 * * * * *")
    public void sendDailyMessage() {
        Map<Long, List<String>> messages = schedule.MessagingQueue();
        for (Map.Entry<Long, List<String>> entry : messages.entrySet()) {
            Long userId = entry.getKey();
            List<String> userMessage = entry.getValue();
            List<String> AllTranslation;
            AllTranslation= wordRepository.getTranslationsByUserId(userId);

            for (int i = 0; i < userMessage.size(); i++) {
                List<String> AllIncorrectTranslation = new ArrayList<>(AllTranslation); // копия
                Collections.shuffle(AllIncorrectTranslation);
                List<String> SelectedIncorrectTranslation = AllIncorrectTranslation.subList(0, 3);
                String correctAnswer = wordRepository.getTranslationByUserIdAndWord(userId, userMessage.get(i));
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
                keyboard.add(buttons);
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                markup.setKeyboard(keyboard);

                SendMessage message = new SendMessage();
                message.setChatId(userId.toString());
                message.setText("Привет, выбери правильный перевод слова: \n" + userMessage.get(i));

                try {
                    message.setReplyMarkup(markup);
                    sender.send(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
