package core.ghayoun.mygitai.git.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) //여기도 히히
public class GitRequest {
    private Repo repo;
    private String ref;
    private List<String> messages;
    private List<FileChange> files;
}