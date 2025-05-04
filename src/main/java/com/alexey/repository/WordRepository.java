package com.alexey.repository;

import com.alexey.models.Word;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByWordAndTranslation(String word, String translation);
    boolean existsByWordAndTranslation (String word, String translation);
    @Modifying
    @Transactional
    @Query(value = "UPDATE word SET delay_between = CURRENT_DATE - date_time", nativeQuery = true)
    void updateAllDelayBetween();



}
