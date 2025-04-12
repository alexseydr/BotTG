package com.alexey.service;
import com.alexey.models.Word;
import com.alexey.repository.WordRepository;

import com.alexey.models.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class WordService {
    private final WordRepository wordRepository;

@Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    //Получаем все слова
    public List<Word> GetAllWords() {
    return wordRepository.findAll();
    }

    //Получаем слово по его Id
    public Optional<Word> GetWordById(Long id) {
    return wordRepository.findById(id);
    }

    //Сохранить слово
    public Word SaveWord(Word word) {
        return wordRepository.save(word);
    }


    //Удалить слово по Id
    public void DeleteWord(Long id) {
    wordRepository.deleteById(id);
    }

}
