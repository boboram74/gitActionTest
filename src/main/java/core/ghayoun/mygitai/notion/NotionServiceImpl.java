package core.ghayoun.mygitai.notion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.ghayoun.mygitai.git.domain.GitRequest;
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
    public ResponseEntity<String> postMessage(GitRequest data, String userJson ,String llmResponse) throws Exception {
        String author = (data != null && data.getRepo() != null && data.getRepo().getOwner() != null)
                ? data.getRepo().getOwner() : "";
        String commitName = (data != null && data.getMessages() != null && !data.getMessages().isEmpty())
                ? String.valueOf(data.getMessages().get(0)) : "";
        ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String originalBlock = "";
        String changedBlock  = "";
        if (userJson != null && !userJson.isBlank()) {
            String cleaned = userJson.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceFirst("^```(?:json)?\\s*", "");
                cleaned = cleaned.replaceFirst("\\s*```$", "");
            }
            com.fasterxml.jackson.databind.JsonNode uj = mapper.readTree(cleaned);
            originalBlock = uj.path("original_block").asText("");
            changedBlock  = uj.path("changed_block").asText("");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(key);
        headers.add("Notion-Version", NOTION_VERSION);

        Map<String, Object> properties = new java.util.LinkedHashMap<>();
        properties.put("작성자", Map.of(
                "title", List.of(Map.of("text", Map.of("content", author)))
        ));
        properties.put("커밋명", Map.of(
                "rich_text", List.of(Map.of("text", Map.of("content", commitName)))
        ));
        properties.put("기존파일명과 기존 코드", Map.of(
                "rich_text", List.of(Map.of("text", Map.of("content", originalBlock)))
        ));
        properties.put("변경파일명과 변경 코드", Map.of(
                "rich_text", List.of(Map.of("text", Map.of("content", changedBlock)))
        ));
        properties.put("요약", Map.of(
                "rich_text", List.of(Map.of("text", Map.of("content", llmResponse)))
        ));
        Map<String, Object> payload = Map.of(
                "parent", Map.of("database_id", databaseId),
                "properties", properties
        );
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(payload, headers);
        return restTemplate.postForEntity(url, req, String.class);
    }
}