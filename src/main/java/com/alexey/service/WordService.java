package com.alexey.service;
import com.alexey.repository.WordRepository;

import com.alexey.models.word;
import com.alexey.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<word> GetAllWords() {
    return wordRepository.findAll();
    }

    //Получаем слово по его Id
    public Optional<word> GetWordById(Long id) {
    return wordRepository.findById(id);
    }

    //Сохранить слово
    public word SaveWord(word word) {
        return wordRepository.save(word);
    }


    //Удалить слово по Id
    public void DeleteWord(Long id) {
    wordRepository.deleteById(id);
    }

}
