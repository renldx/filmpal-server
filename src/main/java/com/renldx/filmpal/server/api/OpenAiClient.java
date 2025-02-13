package com.renldx.filmpal.server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renldx.filmpal.server.entity.Genres;
import com.renldx.filmpal.server.entity.MovieDto;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class OpenAiClient {
    private final SimpleOpenAI simpleOpenAI;
    private final ObjectMapper objectMapper;

    public OpenAiClient(ObjectMapper objectMapper) {
        this.simpleOpenAI = SimpleOpenAI.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();

        this.objectMapper = objectMapper;
    }

    private String buildUserMessage(Genres genre, Collection<MovieDto> watchedMoviesList) {
        var watchedMovies = "";

        if (watchedMoviesList.isEmpty()) {
            watchedMovies = "none";
        } else {
            watchedMovies = watchedMoviesList.stream().map(MovieDto::getTitle).collect(Collectors.joining(", "));
        }

        return String.format("Excluding the following: %s; list the latest top 5 %s movies and their release dates (PST) in ISO 8601 format.", watchedMovies, genre);
    }

    private ChatRequest buildChatRequest(String userMessage) {
        return ChatRequest.builder()
                .model("gpt-4o-mini")
                .message(ChatMessage.SystemMessage.of("You are a movie enthusiast."))
                .message(ChatMessage.UserMessage.of(userMessage))
                .responseFormat(ResponseFormat.jsonSchema(ResponseFormat.JsonSchema.builder()
                        .name("OpenAiResponse")
                        .schemaClass(OpenAiResponse.class)
                        .build()))
                .build();
    }

    private String sendChatRequest(ChatRequest chatRequest) {
        var futureChat = simpleOpenAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();

        return chatResponse.firstContent();
    }

    public OpenAiResponse getChatResponse(Genres genre, Collection<MovieDto> watchedMoviesList) throws JsonProcessingException {
        var userMessage = buildUserMessage(genre, watchedMoviesList);
        var chatRequest = buildChatRequest(userMessage);
        var jsonResponse = sendChatRequest(chatRequest);

        return objectMapper.readValue(jsonResponse, OpenAiResponse.class);
    }
}
