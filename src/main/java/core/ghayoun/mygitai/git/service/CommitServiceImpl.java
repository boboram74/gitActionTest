package core.ghayoun.mygitai.git.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.ghayoun.mygitai.git.domain.*;
import core.ghayoun.mygitai.notion.NotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.info("실행횟수 : {}",count.incrementAndGet());
        long startTime = System.currentTimeMillis();

        Map<String, String> fileToDelta = toMapFromPojo(data);
        String fileChangeResult = objectMapper.writeValueAsString(fileToDelta);
        log.info("AI요청 : " + fileChangeResult);
        List<Message> messages = Arrays.asList(
                new Message(
                        "system",
                        """
                      너는 '코드 변경 요약' 작성가다. 한국어만 사용.
                      출력은 딱 **한 문장**만(마침표 포함) 생성한다. 백틱/코드블록/머릿말 금지.
                      '+' 코드 추가, '-' 코드 삭제.
                      
                      작성 규칙(아주 중요):
                      1) **구체성**: 파일명(최소 1개) 또는 주요 클래스/메서드명을 반드시 포함.
                      2) **행동 동사**: '추가/삭제/수정/리팩터/정리' 중 하나 이상 명시.
                      3) 주석·로그·서식만 바뀐 경우 사실대로 “주석 보강”, “로그 문구 조정” 등으로 기술.
                      4) **여러 파일**(2개 이상): ‘A 등 N개 파일’ 형식. A는 가장 핵심/변경량이 큰 파일.
                      5) 테스트/문서/빌드 변경은 각각 ‘테스트 추가’, ‘문서 수정’, ‘빌드 설정 변경’으로 명확히.
                      6) **추측 금지**: 입력에 없는 기능·동작을 만들지 말 것. “요약 제공 불가” 같은 문구 금지.
                      7) **길이**: 30자~80자.
                      8) **금지어**: ‘제공’, ‘불가’, ‘일반적’, ‘항목’, ‘변경되었습니다’ (단독/상투적 표현) 백틱, 작은따옴표 사용 금지.
                      
                      판단 힌트:
                      - 메서드 시그니처/어노테이션(@RestController, @PostMapping, @Bean, @Value 등) 변경은 컴포넌트명을 드러낼 것.
                      - 필드/생성자/메서드 추가·삭제는 그 사실을 명시.

                      나쁜 예(금지):
                      - 커밋 내용이 변경되었습니다
                      - 제공된 코드 변경 사항에 대한 요약을 제공할 수 없습니다
                      """
                ),
                new Message("user", fileChangeResult)
        );
        Options options = new Options(16, 0.2, 2048, 2048);
        OllamaRequest requestPayload = new OllamaRequest(model, messages, false, options);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OllamaRequest> requestEntity = new HttpEntity<>(requestPayload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        String llmResponse = rootNode.path("message").path("content").asText();
        log.info("AI 응답 = {}", llmResponse);
        notionService.postMessage(data,fileChangeResult,llmResponse);
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