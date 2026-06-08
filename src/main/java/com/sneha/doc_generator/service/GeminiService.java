package com.sneha.doc_generator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateDocs(String code, String language, String docType) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String prompt = switch (docType) {
            case "readme" -> "Generate a professional README.md for this " + language + " code. Include description, setup, and usage.\n\nCode:\n```" + language + "\n" + code + "\n```";
            case "comments" -> "Add detailed inline comments to every function in this " + language + " code. Return only the commented code.\n\nCode:\n```" + language + "\n" + code + "\n```";
            case "apidocs" -> "Generate API documentation for this " + language + " code. Include endpoints, parameters, responses.\n\nCode:\n```" + language + "\n" + code + "\n```";
            default -> "Generate documentation for this " + language + " code.\n\nCode:\n```" + language + "\n" + code + "\n```";
        };

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");
        body.put("max_tokens", 1000);
        body.put("messages", List.of(message));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            "https://api.groq.com/openai/v1/chat/completions",
            HttpMethod.POST,
            request,
            Map.class
        );

        Map responseBody = response.getBody();
        List<Map> choices = (List<Map>) responseBody.get("choices");
        Map messageResponse = (Map) choices.get(0).get("message");
        return (String) messageResponse.get("content");
    }
}