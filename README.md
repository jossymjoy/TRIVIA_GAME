# Trivia Game REST API

This project is a Spring Boot-based REST API for a Trivia Game. It allows users to start a trivia game with a random question fetched from a public API, and respond with an answer. The API keeps track of user attempts and manages correct and incorrect responses.
Here I am using H2 database.

## Features

- **Start Trivia**: Fetches a random trivia question from an external API and returns it to the user.
- **Reply to Trivia**: Allows the user to submit an answer to the trivia question, checking for correctness.
- **Tracks Attempts**: Tracks the number of incorrect attempts. If a user exceeds 3 incorrect attempts, they are blocked from further attempts.
- **Error Handling**: Proper error handling for missing trivia, wrong answers, and exceeding maximum attempts.

## Endpoints

### POST `/trivia/start`

Starts a new trivia game by fetching a random trivia question from an external API.

#### Response
- **Status: 200 OK**
- **Body**:
    ```json
    {
      "triviaId": 1,
      "question": "Which soccer team won the Copa America 2015 Championship?",
      "possibleAnswers": ["Argentina", "Chile", "Paraguay", "Brazil"]
    }
    ```

### PUT `/trivia/reply/{id}`

Submit an answer for a given trivia question.

#### Request
- **Body**:
    ```json
    {
      "answer": "Chile"
    }
    ```

#### Responses
- **Status: 200 OK** (Correct answer)
    ```json
    {
      "result": "right!"
    }
    ```

- **Status: 400 Bad Request** (Wrong answer)
    ```json
    {
      "result": "wrong!"
    }
    ```

- **Status: 403 Forbidden** (Max attempts reached)
    ```json
    {
      "result": "Max attempts reached!"
    }
    ```

- **Status: 404 Not Found** (Trivia not found)
    ```json
    {
      "result": "No such question!"
    }
    ```

## Error Handling

- **404 Not Found**: If a trivia question does not exist for the provided `triviaId`.
- **400 Bad Request**: If the answer is incorrect.
- **403 Forbidden**: If the user has already attempted 3 incorrect answers.
- **500 Internal Server Error**: For any unexpected internal errors.

## Running the Project

### Prerequisites

- Java 17+
- Maven

### Steps to Run

1. Clone the repository:
    ```bash
    git clone https://github.com/jossymjoy/TRIVIA_GAME.git
    cd TRIVIA_GAME
    ```

2. Install dependencies and build the project:
    ```bash
    mvn clean install
    ```

3. Run the application:
    ```bash
    mvn spring-boot:run
    ```

4. The application will be accessible at `http://localhost:8080`.

## Testing

The project uses **JUnit** and **MockMvc** for testing.

To run tests, execute:
```bash
mvn test