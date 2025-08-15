package core.ghayoun.mygitai.notion;

import org.springframework.http.ResponseEntity;

public interface NotionService {
    ResponseEntity<String> postMessage(String data) throws Exception;
}
