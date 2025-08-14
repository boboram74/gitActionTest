package core.ghayoun.mygitai.git.controller;

import core.ghayoun.mygitai.git.domain.GitRequest;
import core.ghayoun.mygitai.git.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/git")
@RequiredArgsConstructor
public class CommitController {

    private final CommitService commitService;

    @ResponseBody
    @PostMapping
    public ResponseEntity<String> commit(@RequestBody GitRequest data) throws Exception {
        return commitService.getMessage(data.getData());
    }
}