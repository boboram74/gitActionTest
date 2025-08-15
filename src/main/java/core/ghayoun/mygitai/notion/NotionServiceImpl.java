package core.ghayoun.mygitai.notion;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotionServiceImpl implements NotionService{

    private final RestTemplate restTemplate;

    @Value("${notion.api.key}")
    private String key;

    @Value("${notion.api.url}")
    private String url;

    @Value("${notion.database.id}")
    private String databaseId;

    private static final String NOTION_VERSION = "2022-06-28";

    @Override
    public ResponseEntity<String> postMessage(String data) throws Exception {
        String title   = data;
        String oldCode = "- old line 1\n- old line 2";
        String newCode = "+ new line 1\n+ new line 2";
        String summary = "기능 A의 예외 처리 강화";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(key);
        headers.add("Notion-Version", NOTION_VERSION);

        Map<String, Object> properties = new java.util.LinkedHashMap<>();
        properties.put("커밋한사람/커밋명", Map.of(
                "title", List.of(Map.of("text", Map.of("content", title)))
        ));
        properties.put("기존파일명과 기존 코드", Map.of(
                "rich_text", List.of(Map.of("text", Map.of("content", oldCode)))
        ));
        properties.put("변경파일명과 변경 코드", Map.of(
                "rich_text", List.of(Map.of("text", Map.of("content", newCode)))
        ));
        properties.put("요약", Map.of(
                "rich_text", List.of(Map.of("text", Map.of("content", summary)))
        ));

        Map<String, Object> payload = Map.of(
                "parent", Map.of("database_id", databaseId),
                "properties", properties
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(payload, headers);
        return restTemplate.postForEntity(url, req, String.class);
    }
}