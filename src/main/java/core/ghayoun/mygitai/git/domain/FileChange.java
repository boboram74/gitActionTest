package core.ghayoun.mygitai.git.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileChange {
    private String filename;
    private String previousFilename;
    private String status;
    private Integer additions;
    private Integer deletions;
    private Integer changes;
    private String delta;
//    private String patch;
//    private String beforeText;
//    private String afterText;
}
