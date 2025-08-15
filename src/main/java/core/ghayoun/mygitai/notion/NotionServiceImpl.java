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
    private static final String PAGES_URL = "https://api.notion.com/v1/pages";

    @Override
    public ResponseEntity<String> postMessage(String data) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(key);
        headers.add("Notion-Version", NOTION_VERSION);
        Map<String, Object> payload = Map.of(
                "parent", Map.of("database_id", databaseId),
                "properties", Map.of(
                        "Name", Map.of(
                                "title", List.of(
                                        Map.of("text", Map.of("content", data))
                                )
                        )
                )
        );
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(payload, headers);
        return restTemplate.postForEntity(PAGES_URL, req, String.class);
    }
}
