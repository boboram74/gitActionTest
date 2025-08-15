package core.ghayoun.mygitai.git.domain;

import lombok.Data;

@Data
public class FileChange {
    private String filename;
    private String status;
    private Integer additions;
    private Integer deletions;
    private Integer changes;
}
