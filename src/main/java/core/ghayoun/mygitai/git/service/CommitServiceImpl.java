package core.ghayoun.mygitai.git.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.ghayoun.mygitai.git.domain.Message;
import core.ghayoun.mygitai.git.domain.OllamaRequest;
import core.ghayoun.mygitai.git.domain.Options;
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
import java.util.List;
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
    public ResponseEntity<String> getMessage(String data) throws Exception{
        System.out.println("실행횟수 = " + count.incrementAndGet());
        long startTime = System.currentTimeMillis();
        List<Message> messages = Arrays.asList(
                new Message("system","너의 답변은 반드시 한국어만 사용한다. " +
                        "영어/로마자/번역문 안내 금지. 영어가 섞였다고 판단되면 스스로 한국어로 다시 작성해 출력한다. " +
                        "코드가 필요할 때만 코드블록을 쓰고, 자연어 설명은 전부 한국어로 작성한다.\n" +
                        "영어 단어(‘Summary’, ‘Key features’)가 포함되면 오답으로 간주하고 다시 한국어로 출력" +
                        "역할: 커밋 요약 전문가. 규칙: '-'는 삭제, '+'는 추가.\n" +
                        "항상 아래 형식을 지켜라 제목 뒤:에 너의 답변을 작성해라\n" +
                        "1. 커밋한사람/커밋명:\n" +
                        "2. 기존파일명과 기존 코드:\n" +
                        "3. 변경파일명과 변경 코드:\n" +
                        "4. 커밋으로 변경점 요약:"),
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

//        ResponseEntity<String> stringResponseEntity = notionService.postMessage(data);
//        System.out.println("노션 응답 : "+stringResponseEntity.getBody());

        System.out.println(response.getBody());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double durationSec = duration / 1000.0;
        System.out.println("실행시간 : " + durationSec + "초");
        return ResponseEntity.ok(data);
    }
}