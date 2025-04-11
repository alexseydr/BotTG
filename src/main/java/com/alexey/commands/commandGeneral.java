package com.alexey.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
public interface commandGeneral {
    String getCommand();
    public void execute (Update update);
}
