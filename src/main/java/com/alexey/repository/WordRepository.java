package com.alexey.repository;

import com.alexey.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByWordAndTranslationAndUserId(String word, String translation,String userId);
    boolean existsByWordAndTranslationAndUserId(String word, String translation, String UserId);
    @Modifying
    @Transactional
    @Query(value = "UPDATE word SET delay_between = CURRENT_DATE - date_time", nativeQuery = true)
    void updateAllDelayBetween();
    @Transactional
    @Query(value = "SELECT  w.translation FROM Word w WHERE CAST(w.user_id AS bigint) = :user_id", nativeQuery = true)
    List <String> getTranslationsByUserId(@Param("user_id") Long userId);
    @Transactional
    @Query(value = "SELECT  w.translation FROM Word w WHERE CAST(w.user_id AS bigint) = :user_id LIMIT 1", nativeQuery = true)
    String getTranslationByUserId(@Param("user_id") Long userId);
    @Transactional
    @Query(value = "SELECT w.translation FROM Word w WHERE CAST(w.user_id AS bigint) = :user_id AND w.word != :word LIMIT 1", nativeQuery = true)
    String getTranslationsExcludingWord(@Param("user_id") Long userId, @Param("word") String word);
    @Transactional
    @Query(value = "SELECT w.translation FROM Word w WHERE CAST(w.user_id AS bigint) = :user_id AND w.word = :word LIMIT 1", nativeQuery = true)
    String getTranslationByUserIdAndWord(@Param("user_id") Long userId, @Param("word") String word);
    @Transactional
    @Query("SELECT w.word FROM Word w WHERE w.userId = :userId AND w.DelayBetween IN (1, 3, 7,9,14,21,30)")
    List<String> getWordsByUserIdAndDelayBetween(@Param("userId") String userId);
    @Transactional
    @Query("SELECT w.userId FROM Word w")
    List<Long> getAllUserId();
    @Transactional
    @Query(value = "SELECT word, translation FROM word WHERE user_id = :userId", nativeQuery = true)
    List<Object[]> getWordsAndTranslationsByUserId(@Param("userId") String userId);









}
