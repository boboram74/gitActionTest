package core.ghayoun.mygitai.git.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.ghayoun.mygitai.git.domain.Message;
import core.ghayoun.mygitai.git.domain.OllamaRequest;
import core.ghayoun.mygitai.git.domain.Options;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class CommitServiceImpl implements CommitService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AtomicInteger count = new AtomicInteger(0);

    @Value("${llm.api.url}")
    private String url;

    @Value("${llm.api.model}")
    private String model;

    @Value("${llm.api.prompt}")
    private String prompt;
    
    @Override
    public ResponseEntity<String> getMessage(String data) throws Exception{
        System.out.println("실행횟수 = " + count.incrementAndGet());
        long startTime = System.currentTimeMillis();
        List<Message> messages = Arrays.asList(
                new Message("system", prompt),
                new Message("user", data)
        );
        System.out.println("요청한 프롬프트 = " + prompt);
        Options options = new Options(16, 0.0, 2048, 2048);
        OllamaRequest requestPayload = new OllamaRequest(model, messages, false, options);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OllamaRequest> requestEntity = new HttpEntity<>(requestPayload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        JsonNode rootNode = objectMapper.readTree(response.getBody());
        data = rootNode.path("message").path("content").asText();

        System.out.println(response.getBody());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double durationSec = duration / 1000.0;
        System.out.println("실행시간 : " + durationSec + "초");
        return ResponseEntity.ok(data);
    }
}