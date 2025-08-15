package core.ghayoun.mygitai.git.service;

import core.ghayoun.mygitai.git.domain.GitRequest;
import org.springframework.http.ResponseEntity;

public interface CommitService {
    ResponseEntity<String> getMessage(GitRequest data) throws Exception;
}