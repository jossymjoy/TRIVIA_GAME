package com.triviagame.trivia.controller;

import com.triviagame.trivia.model.Trivia;
import com.triviagame.trivia.service.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trivia")
public class TriviaController {

    @Autowired
    private TriviaService triviaService;

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startTrivia() {
        Trivia trivia = triviaService.startTrivia();

        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add(trivia.getCorrectAnswer());

        Map<String, Object> response = new HashMap<>();
        response.put("triviaId", trivia.getTriviaId());
        response.put("question", trivia.getQuestion());
        response.put("possibleAnswers", possibleAnswers);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/reply/{id}")
    public ResponseEntity<Map<String, Object>> replyTrivia(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String answer = requestBody.get("answer");
        Map<String, Object> result = triviaService.checkAnswer(id, answer);

        return new ResponseEntity<>(result, HttpStatus.valueOf((Integer) result.get("status")));
    }
}
