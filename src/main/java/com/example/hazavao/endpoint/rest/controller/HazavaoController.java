//package com.example.hazavao.endpoint.rest.controller;
//
//import com.example.hazavao.PojaGenerated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.beans.factory.annotation.Value;
//
//@PojaGenerated // Annotation Poja pour les fichiers générés
//@RestController
//public class HazavaoController {
//
//    @Value("${openai.api.key}") // Clé injectée depuis les secrets GitHub
//    private String apiKey;
//
//    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
//
//    @GetMapping("/hazavao")
//    public String getDefinition(@RequestParam String teny) {
//        // 1. Préparer les headers avec la clé API
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + apiKey);
//        headers.set("Content-Type", "application/json");
//
//        // 2. Construire le corps de la requête pour ChatGPT
//        String requestBody = String.format(
//                "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"Donne la définition en malgache du mot '%s'\"}]}",
//                teny);
//
//        // 3. Envoyer la requête à l'API OpenAI
//        ResponseEntity<String> response = new RestTemplate().exchange(
//                OPENAI_URL,
//                HttpMethod.POST,
//                new HttpEntity<>(requestBody, headers),
//                String.class);
//
//        // 4. Retourner la réponse de ChatGPT
//        return response.getBody();
//    }
//}


package com.example.hazavao.endpoint.rest.controller;

import com.example.hazavao.PojaGenerated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger; // <--- ADD THIS IMPORT
import org.slf4j.LoggerFactory; // <--- ADD THIS IMPORT

@PojaGenerated
@RestController
public class HazavaoController {

    private static final Logger logger = LoggerFactory.getLogger(HazavaoController.class); // <--- ADD THIS LINE

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    @GetMapping("/hazavao")
    public String getDefinition(@RequestParam String teny) {
        // Log the key *after* it has been injected by Spring
        logger.info("OpenAI API Key being used (first 5 chars): {}", apiKey.substring(0, Math.min(apiKey.length(), 5)));
        // Optionally, you could log the full key if you're sure about your environment,
        // but for security, usually only partial logging is done.
        // logger.info("Full API Key: {}", apiKey); // USE WITH CAUTION!

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String requestBody = String.format(
                "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"Donne la définition en malgache du mot '%s'\"}]}",
                teny);

        try {
            ResponseEntity<String> response = new RestTemplate().exchange(
                    OPENAI_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    String.class);
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error calling OpenAI API for word '{}': {}", teny, e.getMessage(), e);
            throw new RuntimeException("Failed to get definition from OpenAI: " + e.getMessage(), e);
        }
    }
}