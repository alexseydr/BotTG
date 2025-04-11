package com.alexey.service;

import com.alexey.models.word;
import com.alexey.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class WordController {
private final WordService wordService;
@Autowired
    public WordController(WordService wordService) {
    this.wordService = wordService;

}
@GetMapping
    public List<word> getWords() {
    return wordService.GetAllWords();
}
@GetMapping("/{id}")
    public Optional<word> GetWordById (@PathVariable long id) {
    return wordService.GetWordById(id);
}
@PostMapping
    public word SaveWord (@RequestBody word word) {
    return wordService.SaveWord(word);
}
@DeleteMapping
    public void DeleteWord (@PathVariable long id) {
    wordService.DeleteWord(id);
}

}
