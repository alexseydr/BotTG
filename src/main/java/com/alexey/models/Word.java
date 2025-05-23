package com.alexey.models;


import jakarta.persistence.*;

import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "word",
        uniqueConstraints = @UniqueConstraint(columnNames = {"word", "translation","user_id"}))
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID будет уникальным для каждого слова
    private LocalDate dateTime; // Дата добавления слова
    private String word;  // Слово
    private String translation;  // Перевод
    private Integer DelayBetween; // Время в днях от добавления до нынешней даты для установления расписания повторения 1,3,7,14 дней
    @Column(name = "user_id")
    private String userId;
    // Конструктор без параметров
    public Word() {
    }

    // Конструктор с параметрами
    public Word(Long id, String word, String translation,LocalDate dateTime, Integer DelayBetween,String userId) {
        this.id = id;
        this.word = word;
        this.translation = translation;
        this.DelayBetween = DelayBetween;
        this.dateTime = dateTime;
        this.userId = userId;

    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
    public int getDelayBetween() {
        return (int) ChronoUnit.DAYS.between(this.dateTime, LocalDate.now());
    }

    public String getUserId() {
        return userId;
    }
}