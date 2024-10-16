package com.triviagame.trivia.repository;

import com.triviagame.trivia.model.Trivia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriviaRepository extends JpaRepository<Trivia, Long> {
}
