package com.alexey.MessageSender;

import org.springframework.scheduling.annotation.Scheduled;


public class Schedule {
    @Scheduled(cron = "0 0 6 * * *" ) //секунды минуты часы(06:00) день месяц деньнедели
    public void sdsd (){


    }


}
