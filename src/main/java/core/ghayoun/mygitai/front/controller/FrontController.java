package core.ghayoun.mygitai.front.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class FrontController {

    @GetMapping
    public ResponseEntity<String> home () {
        return ResponseEntity.ok("ㅎㅇ 나는 서버2야");
    }
}