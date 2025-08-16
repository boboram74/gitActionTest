package core.ghayoun.mygitai.notion;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.ghayoun.mygitai.git.domain.GitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public void postMessage(GitRequest data, String fileChangeResult, String llmResponse) throws Exception {
        String author = (data != null && data.getRepo() != null && data.getRepo().getOwner() != null)
                ? data.getRepo().getOwner() : "";
        String commitName = (data != null && data.getMessages() != null && !data.getMessages().isEmpty())
                ? String.valueOf(data.getMessages().get(0)) : "";

        String originalBlock = "";
        String changedBlock  = "";

        if (fileChangeResult != null && !fileChangeResult.isBlank()) {
            ObjectMapper m = new ObjectMapper();
            Map<String, String> diffMap = m.readValue(
                    fileChangeResult,
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {}
            );

            StringBuilder minusAll = new StringBuilder();
            StringBuilder plusAll  = new StringBuilder();

            for (Map.Entry<String, String> e : diffMap.entrySet()) {
                String file = e.getKey();
                String diff = Objects.toString(e.getValue(), "");

                StringBuilder minusPerFile = new StringBuilder();
                StringBuilder plusPerFile  = new StringBuilder();
                boolean hasMinus = false, hasPlus = false;

                for (String line : diff.split("\\r?\\n")) {
                    if (line.startsWith("-")) {
                        if (!hasMinus) { minusPerFile.append("# ").append(file).append('\n'); hasMinus = true; }
                        minusPerFile.append(line).append('\n');
                    } else if (line.startsWith("+")) {
                        if (!hasPlus)  { plusPerFile.append("# ").append(file).append('\n');  hasPlus  = true; }
                        plusPerFile.append(line).append('\n');
                    }
                }
                if (hasMinus) minusAll.append(minusPerFile);
                if (hasPlus)  plusAll.append(plusPerFile);
            }
            originalBlock = minusAll.toString().trim();
            changedBlock  = plusAll.toString().trim();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(key);
        headers.add("Notion-Version", NOTION_VERSION);

        String previewSuffix = "\n… (전체 내용은 본문 코드 블록 참조)";
        String originalPreview = truncateForProperty(originalBlock, 2000, previewSuffix);
        String changedPreview  = truncateForProperty(changedBlock,  2000, previewSuffix);

        Map<String, Object> properties = new java.util.LinkedHashMap<>();
        properties.put("작성자", Map.of(
                "title", List.of(Map.of("text", Map.of("content", author)))
        ));
        properties.put("커밋명", Map.of(
                "rich_text", List.of(Map.of("text", Map.of("content", commitName)))
        ));
        properties.put("기존파일명과 기존 코드", Map.of(
                "rich_text", toRichTextParts(originalPreview)
        ));
        properties.put("변경파일명과 변경 코드", Map.of(
                "rich_text", toRichTextParts(changedPreview)
        ));
        properties.put("요약", Map.of(
                "rich_text", toRichTextParts(Objects.toString(llmResponse, ""))
        ));

        List<Map<String, Object>> children = new java.util.ArrayList<>();
        if (!originalBlock.isEmpty()) {
            children.add(headingBlock("기존파일/삭제 라인"));
            children.add(codeBlock(originalBlock));
        }
        if (!changedBlock.isEmpty()) {
            children.add(headingBlock("변경파일/추가 라인"));
            children.add(codeBlock(changedBlock));
        }

        Map<String, Object> payload = Map.of(
                "parent", Map.of("database_id", databaseId),
                "properties", properties,
                "children", children
        );



        HttpEntity<Map<String, Object>> req = new HttpEntity<>(payload, headers);
        restTemplate.postForEntity(url, req, String.class);
    }

    private static List<Map<String, Object>> toRichTextParts(String s) {
        if (s == null) s = "";
        final int LIMIT = 2000;
        List<Map<String, Object>> parts = new java.util.ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            int end = Math.min(i + LIMIT, s.length());
            int soft = Math.max(i, end - 200);
            int nl = s.lastIndexOf('\n', end - 1);
            if (nl >= soft) end = nl + 1;

            String chunk = s.substring(i, end);
            parts.add(Map.of("type","text","text", Map.of("content", chunk)));
            i = end;
        }
        if (parts.isEmpty()) {
            parts.add(Map.of("type","text","text", Map.of("content", "")));
        }
        return parts;
    }
    private static Map<String,Object> codeBlock(String text) {
        return Map.of(
                "object", "block",
                "type", "code",
                "code", Map.of(
                        "language", "java",
                        "rich_text", toRichTextParts(text)   // 2000자 조각으로 자동 분할
                )
        );
    }

    private static Map<String, Object> headingBlock(String text) {
        return Map.of(
                "object", "block",
                "type", "heading_3",
                "heading_3", Map.of(
                        "rich_text", List.of(
                                Map.of("type","text","text", Map.of("content", text))
                        )
                )
        );
    }

    private static String truncateForProperty(String s, int limit, String suffixIfTrimmed) {
        if (s == null) return "";
        if (s.length() <= limit) return s;
        int keep = Math.max(0, limit - suffixIfTrimmed.length());
        return s.substring(0, keep) + suffixIfTrimmed;
    }

}