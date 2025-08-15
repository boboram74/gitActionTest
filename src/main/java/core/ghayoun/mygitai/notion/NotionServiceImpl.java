package core.ghayoun.mygitai.notion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        String cleaned = data == null ? "" : data.trim();
        if (cleaned.startsWith("```")) {
            // 앞/뒤의 ``` 또는 ```json 제거 (있으면)
            cleaned = cleaned.replaceFirst("^```(?:json)?\\s*", "");
            cleaned = cleaned.replaceFirst("\\s*```$", "");
        }
        int s = cleaned.indexOf('{');
        int e = cleaned.lastIndexOf('}');
        data = (s >= 0 && e > s) ? cleaned.substring(s, e + 1) : "{}";

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> m = mapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        String authorAndTitle = String.valueOf(m.getOrDefault("author_and_title", ""));
        String originalBlock  = String.valueOf(m.getOrDefault("original_block", ""));
        String changedBlock   = String.valueOf(m.getOrDefault("changed_block", ""));
        String summary        = String.valueOf(m.getOrDefault("summary", ""));
        System.out.println("authorAndTitle = " + authorAndTitle);
        System.out.println("originalBlock = " + originalBlock);
        System.out.println("changedBlock = " + changedBlock);
        System.out.println("summary = " + summary);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(key);
        headers.add("Notion-Version", NOTION_VERSION);

        Map<String, Object> properties = new java.util.LinkedHashMap<>();
        properties.put("커밋한사람/커밋명", Map.of(
                "title", List.of(Map.of("text", Map.of("content", authorAndTitle)))
        ));
        properties.put("기존파일명과 기존 코드", Map.of(
                "rich_text", List.of(Map.of("text", Map.of("content", originalBlock)))
        ));
        properties.put("변경파일명과 변경 코드", Map.of(
                "rich_text", List.of(Map.of("text", Map.of("content", changedBlock)))
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