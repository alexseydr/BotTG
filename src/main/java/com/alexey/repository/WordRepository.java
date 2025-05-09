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
import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByWordAndTranslation(String word, String translation);
    boolean existsByWordAndTranslation (String word, String translation);
    @Modifying
    @Transactional
    @Query(value = "UPDATE word SET delay_between = CURRENT_DATE - date_time", nativeQuery = true)
    void updateAllDelayBetween();

    @Query("SELECT w.translation FROM Word w WHERE w.UserId = :userId")
    List<String> getTranslationsByUserId(@Param("userId") String userId);

    @Query("SELECT w.word FROM Word w WHERE w.UserId = :userId AND w.DelayBetween IN (0,1, 3, 7)")
    List<String> getWordsByUserIdAndDelayBetween(@Param("userId") String userId);
    @Query("SELECT w.UserId FROM Word w")
    List<Long> getAllUserId();

 






}
