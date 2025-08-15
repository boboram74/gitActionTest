package core.ghayoun.mygitai.git.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Options {
    private int gpu_layers;
    private double temperature;//ㅂㅈㄷ
    private int num_predict;
    private int num_ctx;//ㅂㅈㄷ
}
