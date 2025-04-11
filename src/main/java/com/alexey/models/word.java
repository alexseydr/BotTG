package com.alexey.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class word {
    @Id
    private Long id;  // ID будет уникальным для каждого слова

    private String word;  // Слово
    private String translation;  // Перевод

    // Конструктор без параметров
    public word() {
    }

    // Конструктор с параметрами
    public word(Long id, String word, String translation) {
        this.id = id;
        this.word = word;
        this.translation = translation;
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
}