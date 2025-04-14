package com.alexey.repository;

import com.alexey.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByWordAndTranslation(String word, String translation);
    boolean existsByWordAndTranslation (String word, String translation);

}
