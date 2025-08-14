package core.ghayoun.mygitai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> notHandler(NoResourceFoundException e) {
        log.error("[exceptionHandler] [400] = {}",e.getMessage());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", "NOT_FOUND");
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> ilHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] [404] = {}",e.getMessage());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", "BAD_REQUEST");
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> internalHandler(Exception e) {
        log.error("[exceptionHandler] [500] = {}",e.getMessage());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", "SERVER_ERROR");
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}