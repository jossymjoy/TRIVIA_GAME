package com.triviagame.trivia.controller;

import com.triviagame.trivia.exception.TriviaNotFoundException;
import com.triviagame.trivia.model.Trivia;
import com.triviagame.trivia.service.TriviaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TriviaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TriviaService triviaService;

    @Test
    public void startTrivia_ShouldReturnTriviaQuestion() throws Exception {
        Trivia trivia = new Trivia();
        trivia.setTriviaId(1L);
        trivia.setQuestion("What is 2 + 2?");
        trivia.setCorrectAnswer("4");

        when(triviaService.startTrivia()).thenReturn(trivia);

        mockMvc.perform(post("/trivia/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.triviaId").value(1L))
                .andExpect(jsonPath("$.question").value("What is 2 + 2?"));
    }

    @Test
    public void replyTrivia_ShouldReturnCorrect() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "right!");

        when(triviaService.checkAnswer(1L, "4")).thenReturn(response);

        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\": \"4\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("right!"));
    }

    @Test
    public void replyTrivia_ShouldReturnWrong() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("message", "wrong!");

        when(triviaService.checkAnswer(1L, "5")).thenReturn(response);

        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\": \"5\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("wrong!"));
    }
    @Test
    public void replyTrivia_ShouldReturnTriviaNotFound() throws Exception {
        when(triviaService.checkAnswer(1L, "AnyAnswer"))
                .thenThrow(new TriviaNotFoundException("No such question!"));

        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\": \"AnyAnswer\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result").value("No such question!"));
    }

    @Test
    public void replyTrivia_ShouldReturnInternalServerError() throws Exception {
        when(triviaService.checkAnswer(1L, "AnyAnswer"))
                .thenThrow(new RuntimeException("Something went wrong!"));

        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\": \"AnyAnswer\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Something went wrong!"));
    }
}
