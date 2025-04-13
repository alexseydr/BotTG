package com.alexey.service;

import com.alexey.models.Word;
import com.alexey.repository.WordRepository;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class WordController {
private final WordService wordService;


@Autowired
    public WordController(WordService wordService) {
    this.wordService = wordService;

}
@GetMapping
    public List<Word> getWords() {
    return wordService.GetAllWords();
}
@GetMapping("/{id}")
    public Optional<Word> GetWordById (@PathVariable long id) {
    return wordService.GetWordById(id);
}
@PostMapping("/api/add")
    public Word SaveWord (@RequestBody Word word) {
    return wordService.SaveWord(word);
}


}
