package core.ghayoun.mygitai.notion;

import core.ghayoun.mygitai.git.domain.GitRequest;
import org.springframework.http.ResponseEntity;

public interface NotionService {
    ResponseEntity<String> postMessage(GitRequest data, String fileChangeResult, String llmResponse) throws Exception;
}
