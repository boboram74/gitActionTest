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
    @PostMapping("/commit")
    public ResponseEntity<String> getCommitMessges(@RequestBody GitRequest data) throws Exception {
        ResponseEntity<String> message = commitService.getMessage(data);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }
}