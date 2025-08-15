package core.ghayoun.mygitai.notion;

import core.ghayoun.mygitai.git.domain.GitRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface NotionService {
    ResponseEntity<String> postMessage(GitRequest data, String userJson, String llmResponse) throws Exception;
}
