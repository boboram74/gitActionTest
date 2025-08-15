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
                new Message("system","너는 커밋 요약 봇이다. 한국어만 사용한다.\n" +
                        "\"규칙:\\n\" +\n" +
                        "\" -는 삭제, +는 추가 라인이다.\\n\" +\n" +
                        "\"JSON 이외의 텍스트를 출력하면 안 된다.\"" +
                        "\"출력은 반드시 아래 JSON 하나만 생성한다(코드블록/설명/문장 금지).\n" +
                        "\n" +
                        "{\n" +
                        "  \"author_and_title\": \"아이디/커밋명\",\n" +
                        "  \"original_block\": \"원본파일명과 원본파일내용\",\n" +
                        "  \"changed_block\": \"수정파일명과 수정한파일내용\",\n" +
                        "  \"summary\": \"한 문장 한국어 요약\"\n" +
                        "}\n" +
                        "\n"),
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