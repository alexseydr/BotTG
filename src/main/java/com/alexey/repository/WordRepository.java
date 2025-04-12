package com.alexey.repository;

import com.alexey.models.word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<word, Long> {

}
