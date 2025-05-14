package com.alexey.service;
import com.alexey.models.Word;
import com.alexey.repository.WordRepository;
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
    public  void ListWord (String userId){
        wordRepository.getWordsAndTranslationsByUserId(userId);
    }
    //Для сервиса в ServiceSend
    //public List <String> getTranslationsByUserId(Long userId){
      //  wordRepository.getWordsAndTranslationsByUserId(String.valueOf(userId));
        //return getTranslationsByUserId(userId);
    //}



    //Удалить слово по Id
    public void DeleteWord(String word, String translation) {
        if (wordRepository.findByWordAndTranslation(word, translation).isPresent()) {
            wordRepository.findByWordAndTranslation(word, translation).ifPresent(wordRepository::delete);
        }

    }
}
