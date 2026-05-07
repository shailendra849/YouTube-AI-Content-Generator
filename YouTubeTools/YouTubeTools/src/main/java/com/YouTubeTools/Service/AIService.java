package com.YouTubeTools.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIService {

    private final WebClient.Builder webClientBuilder;

    @Value("${openai.api.key}")
    private String apiKey;

    // Change this to "groq" or "gemini" or keep "openai"
    @Value("${ai.provider:groq}")
    private String aiProvider;

    private static final String GROQ_BASE_URL = "https://api.groq.com/openai/v1";
    private static final String OPENAI_BASE_URL = "https://api.openai.com";

    public List<String> enhanceTags(String videoTitle, List<String> existingTags) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new RuntimeException("AI API key is missing. Please add it in application.properties");
        }

        try {
            String existingTagsStr = existingTags.isEmpty() ? "none" : String.join(", ", existingTags);

            String userPrompt = """
                    I have a YouTube video titled: "%s"
                    Existing tags from similar videos: %s

                    Suggest exactly 10 additional, highly relevant SEO tags for this video.
                    Rules:
                    - Return ONLY a comma-separated list of tags
                    - No numbering, no explanations, no extra text
                    - Each tag should be 1-4 words
                    - Make them specific and searchable
                    """.formatted(videoTitle, existingTagsStr);

            String baseUrl = aiProvider.equalsIgnoreCase("groq") ? GROQ_BASE_URL : OPENAI_BASE_URL;
            String model = aiProvider.equalsIgnoreCase("groq") ? "llama-3.3-70b-versatile" : "gpt-3.5-turbo";

            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "max_tokens", 300,
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are a YouTube SEO expert. Return only comma-separated tags."),
                            Map.of("role", "user", "content", userPrompt)
                    )
            );

            String responseBody = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            String content = root.path("choices").get(0)
                    .path("message")
                    .path("content")
                    .asText().trim();

            return Arrays.stream(content.split(","))
                    .map(String::trim)
                    .filter(tag -> !tag.isBlank())
                    .collect(Collectors.toList());

        } catch (WebClientResponseException e) {
            throw new RuntimeException("AI API error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get AI suggestions: " + e.getMessage());
        }
    }
}