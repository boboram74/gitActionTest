package core.ghayoun.mygitai.git.service;

import org.springframework.http.ResponseEntity;

public interface CommitService {
    ResponseEntity<String> getMessage(String data) throws Exception;
}