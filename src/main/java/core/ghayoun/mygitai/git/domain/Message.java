package core.ghayoun.mygitai.git.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //ㅂㅈㄷ
@AllArgsConstructor
public class Message {
    private String role;
    private String content;
}
