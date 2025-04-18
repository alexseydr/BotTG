package com.alexey.models;


import jakarta.persistence.*;

@Entity
@Table(name = "word", uniqueConstraints = {@UniqueConstraint(columnNames = {"word","translation"})})
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID будет уникальным для каждого слова

    private String word;  // Слово
    private String translation;  // Перевод

    // Конструктор без параметров
    public Word() {
    }

    // Конструктор с параметрами
    public Word(Long id, String word, String translation) {
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