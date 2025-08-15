package core.ghayoun.mygitai.git.controller;

import core.ghayoun.mygitai.git.domain.GitRequest;
import core.ghayoun.mygitai.git.domain.userRequest;
import core.ghayoun.mygitai.git.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/git")
@RequiredArgsConstructor
public class CommitController {

    private final CommitService commitService;

    @ResponseBody
    @PostMapping
    public ResponseEntity<String> getResponseGpt(@RequestBody userRequest data) throws Exception {
        return commitService.getMessage(data.getData());
    }

    @ResponseBody
    @PostMapping("/commit") //자자뭘해볼까
    public ResponseEntity<String> getCommitMessges(@RequestBody GitRequest data) throws Exception {
        System.out.println("데이터원문 = "+data.toString());
        ResponseEntity<String> message = commitService.getMessage(data.toString());
        System.out.println("AI응답 = \n" + message.getBody());
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }
}