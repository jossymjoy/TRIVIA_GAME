package com.triviagame.trivia.service.impl;

import com.triviagame.trivia.exception.TriviaNotFoundException;
import com.triviagame.trivia.model.Trivia;
import com.triviagame.trivia.repository.TriviaRepository;
import com.triviagame.trivia.service.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TriviaServiceImpl implements TriviaService {

    @Autowired
    private TriviaRepository triviaRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Trivia startTrivia() {
        String url = "https://opentdb.com/api.php?amount=1";
        ResponseEntity<Map> repsonse = restTemplate.getForEntity(url, Map.class);

        List<Map> results = (List<Map>) repsonse.getBody().get("results");
        Map<String, Object> triviaData = results.get(0);

        Trivia trivia = new Trivia();
        trivia.setQuestion((String) triviaData.get("question"));
        trivia.setCorrectAnswer((String) triviaData.get("correct_answer"));

        List<String> incorrectAnswers = (List<String>) triviaData.get("incorrect_answers");

        List<String> possibleAnswers = new ArrayList<>(incorrectAnswers);
        possibleAnswers.add(trivia.getCorrectAnswer());

        Collections.shuffle(possibleAnswers);

        trivia.setPossibleAnswers(possibleAnswers);

        return triviaRepository.save(trivia);
    }

    @Override
    public Map<String, Object> checkAnswer(Long triviaId, String answer) {
        Trivia trivia = triviaRepository.findById(triviaId)
                .orElseThrow(() -> new TriviaNotFoundException("No such question!"));

        Map<String, Object> result = new HashMap<>();

        if (trivia.getAnswerAttempts() >= 3) {
            result.put("status", 403);
            result.put("message", "Max attempts reached!");
            return result;
        }

        if (trivia.getCorrectAnswer().equalsIgnoreCase(answer)) {
            triviaRepository.deleteById(triviaId);
            result.put("status", 200);
            result.put("message", "right!");
        } else {
            trivia.setAnswerAttempts(trivia.getAnswerAttempts() + 1);
            triviaRepository.save(trivia);
            result.put("status", 400);
            result.put("message", "wrong!");
        }

        return result;
    }
}
