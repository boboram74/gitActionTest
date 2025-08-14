package core.ghayoun.mygitai.git.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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

@RequiredArgsConstructor
@Service
public class CommitServiceImpl implements CommitService {

    private final RestTemplate restTemplate;

    @Value("${llm.api.url}")
    private String url;

    @Value("${llm.api.model}")
    private String model;

    private int count;

    @Override
    public ResponseEntity<String> getMessage(String data) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        count++;
        System.out.println("실행횟수 = " + count);
        long startTime = System.currentTimeMillis();
        List<Message> messages = Arrays.asList(
                new Message("system", "당신은 여행전문가 입니다. 당신은 친절하게 답변해야합니다. 답변은 무조건 한국어로만 답변해주세요."),
                new Message("user", data)
        );
        Options options = new Options(16, 0.2, 2048, 2048);
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
        System.out.println("실행시간 : " + durationSec);
        return ResponseEntity.ok(data);
    }
}