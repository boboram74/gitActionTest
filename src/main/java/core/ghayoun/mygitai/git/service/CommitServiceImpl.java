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

        List<Message> messages = Arrays.asList(
                new Message(
                        "system",
                        "너는 커밋 요약 봇이다. 한국어만 사용한다.\n" +
                                "무조건 유효한 JSON 하나만 출력한다(코드블록/백틱/설명문 금지). 문자열의 줄바꿈은 \\n 으로 이스케이프한다.\n" +
                                "\n" +
                                "출력 스키마:\n" +
                                "{\n" +
                                "  \"summary\": \"변경된 모든 파일에 대한 한국어 한 문장 요약\"\n" +
                                "}\n" +
                                "\n" +
                                "엄격한 규칙:\n" +
                                "- 입력에 없는 내용을 지어내지 않는다. 줄 내용은 원문 그대로 사용한다.\n"
                ),
                new Message("user", fileToDelta.toString())
        );
        Options options = new Options(16, 0.1, 2048, 2048);
        OllamaRequest requestPayload = new OllamaRequest(model, messages, false, options,"json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OllamaRequest> requestEntity = new HttpEntity<>(requestPayload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println(response.getBody());
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
            // "# 파일명" 헤더 제거
            if (delta.startsWith("#")) {
                int nl = delta.indexOf('\n');
                if (nl != -1) delta = delta.substring(nl + 1);
            }
            out.put(label, delta.trim());
        }
        return out;
    }
}