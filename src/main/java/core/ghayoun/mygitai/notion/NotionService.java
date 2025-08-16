package core.ghayoun.mygitai.notion;

import core.ghayoun.mygitai.git.domain.GitRequest;

public interface NotionService {
    void postMessage(GitRequest data, String fileChangeResult, String llmResponse) throws Exception;
}
