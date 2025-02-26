package com.renldx.filmpal.server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renldx.filmpal.server.constant.EnvironmentVariables;
import com.renldx.filmpal.server.exception.ApiClientException;
import com.renldx.filmpal.server.model.Genre;
import com.renldx.filmpal.server.model.MovieDto;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@Service
public class OpenAiClient {

    private final SimpleOpenAI simpleOpenAI;
    private final ObjectMapper objectMapper;

    public OpenAiClient(ObjectMapper objectMapper) {
        this.simpleOpenAI = SimpleOpenAI.builder()
                .apiKey(System.getenv(EnvironmentVariables.OPENAI_API_KEY))
                .build();

        this.objectMapper = objectMapper;
    }

    private String buildUserMessage(Genre genre, Collection<MovieDto> watchedMoviesList) {
        var watchedMovies = "";

        if (watchedMoviesList.isEmpty()) {
            watchedMovies = "none";
        } else {
            watchedMovies = watchedMoviesList.stream().map(MovieDto::getTitle).collect(Collectors.joining(", "));
        }

        return String.format("Excluding the following: %s; list the latest best 5 %s feature-length movies and release years.", watchedMovies, genre);
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

    private String sendChatRequest(ChatRequest chatRequest) throws ApiClientException {
        var futureChat = simpleOpenAI.chatCompletions().create(chatRequest);

        try {
            var chatResponse = futureChat.join();
            return chatResponse.firstContent();
        } catch (CompletionException e) {
            throw new ApiClientException(e.getMessage());
        }
    }

    public OpenAiResponse getChatResponse(Genre genre, Collection<MovieDto> watchedMoviesList) throws JsonProcessingException, ApiClientException {
        //var userMessage = buildUserMessage(genre, watchedMoviesList);
        //var chatRequest = buildChatRequest(userMessage);
        //var jsonResponse = sendChatRequest(chatRequest);

        //return objectMapper.readValue(jsonResponse, OpenAiResponse.class);

        // TODO: Fix API access
        var movies = new ArrayList<>(List.of(
                new OpenAiResponse.OpenAiResponseMovie("Anora", "2024"),
                new OpenAiResponse.OpenAiResponseMovie("Oppenheimer", "2023"),
                new OpenAiResponse.OpenAiResponseMovie("Everything Everywhere All at Once", "2022"),
                new OpenAiResponse.OpenAiResponseMovie("Nomadland", "2020"),
                new OpenAiResponse.OpenAiResponseMovie("Dune: Part Two", "2024")
        ));

        return new OpenAiResponse(movies);
    }

    public record OpenAiResponse(Collection<OpenAiResponseMovie> movies) {
        public record OpenAiResponseMovie(String title, String release) {
        }
    }

}
