package com.renldx.filmpal.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renldx.filmpal.entity.Genres;
import com.renldx.filmpal.entity.Movie;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenAiClient {
    private final ObjectMapper objectMapper;
    private final SimpleOpenAI openAI;

    public OpenAiClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        this.openAI = SimpleOpenAI.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();
    }

    public OpenAiResponse GetMovies(Genres genre, List<Movie> watchedMoviesList) throws JsonProcessingException {
        var watchedMovies = "";

        if (watchedMoviesList.isEmpty()) {
            watchedMovies = "none";
        } else {
            watchedMovies = watchedMoviesList.stream().map(Movie::getTitle).collect(Collectors.joining(", "));
        }

        var requestUserMessage = String.format("Excluding the following: %s; list the latest top 5 %s movies and their release dates in UTC format.", watchedMovies, genre);

        var chatRequest = ChatRequest.builder()
                .model("gpt-4o-mini")
                .message(ChatMessage.SystemMessage.of("You are a movie enthusiast."))
                .message(ChatMessage.UserMessage.of(requestUserMessage))
                .responseFormat(ResponseFormat.jsonSchema(ResponseFormat.JsonSchema.builder()
                        .name("OpenAiResponse")
                        .schemaClass(OpenAiResponse.class)
                        .build()))
                .build();

        var futureChat = openAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();
        var jsonResponse = chatResponse.firstContent();

        return objectMapper.readValue(jsonResponse, OpenAiResponse.class);
    }
}
