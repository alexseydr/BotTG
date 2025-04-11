package com.alexey.repository;

import com.alexey.models.word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<word, Long> {

}
