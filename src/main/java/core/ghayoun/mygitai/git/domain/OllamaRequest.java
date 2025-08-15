package core.ghayoun.mygitai.git.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor//ㅂㅈㄷㅂㅈㄷ
@AllArgsConstructor
public class OllamaRequest {
    private String model;
    private List<Message> messages;
    private boolean stream;
    private Options options;
}