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
    @PostMapping("/commit")
    public ResponseEntity<String> getCommitMessges(@RequestBody GitRequest data) throws Exception {
        System.out.println(data);
        String text = String.join("\n", data.getMessages());
        System.out.println(data);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }
}