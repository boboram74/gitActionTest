package core.ghayoun.mygitai.git.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.ghayoun.mygitai.git.domain.*;
import core.ghayoun.mygitai.notion.NotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class CommitServiceImpl implements CommitService {

    private final NotionService notionService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AtomicInteger count = new AtomicInteger(0);

    @Value("${llm.api.url}")
    private String url;

    @Value("${llm.api.model}")
    private String model;

    @Override
    public ResponseEntity<String> getMessage(GitRequest data) throws Exception{
        System.out.println("실행횟수 = " + count.incrementAndGet());
        long startTime = System.currentTimeMillis();

        Map<String, String> fileToDelta = toMapFromPojo(data);
        System.out.println("변환된 파일 = "+fileToDelta);
        String userJson = objectMapper.writeValueAsString(fileToDelta);

        List<Message> messages = Arrays.asList(
                new Message(
                        "system",
                        "너는 커밋 요약 봇이다. 한국어만 사용한다.\n" +
                                "깃 커밋내용을 보내줄테니 요약해줘"
                ),
                new Message("user", userJson)
        );
        Options options = new Options(16, 0.2, 2048, 2048);
        OllamaRequest requestPayload = new OllamaRequest(model, messages, false, options);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OllamaRequest> requestEntity = new HttpEntity<>(requestPayload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println("AI응답 = \n"+response.getBody());
//        notionService.postMessage(data);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double durationSec = duration / 1000.0;
        System.out.println("실행시간 : " + durationSec + "초");
        return ResponseEntity.ok().build();
    }

    Map<String, String> toMapFromPojo(GitRequest req) {
        Map<String, String> out = new LinkedHashMap<>();
        for (FileChange f : req.getFiles()) {
            String label = (f.getPreviousFilename() != null)
                    ? f.getPreviousFilename() + " -> " + f.getFilename()
                    : f.getFilename();

            String delta = f.getDelta();
            if (delta == null) delta = "";
            if (delta.startsWith("#")) {
                int nl = delta.indexOf('\n');
                if (nl != -1) delta = delta.substring(nl + 1);
            }
            out.put(label, delta.trim());
        }
        return out;
    }
}