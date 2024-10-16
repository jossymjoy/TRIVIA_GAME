package com.triviagame.trivia.service;

import com.triviagame.trivia.model.Trivia;

import java.util.Map;

public interface TriviaService {
    public Trivia startTrivia();
    public Map<String, Object> checkAnswer(Long triviaId, String answer);
}
