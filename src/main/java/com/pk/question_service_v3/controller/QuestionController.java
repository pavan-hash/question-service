package com.pk.question_service_v3.controller;



import com.pk.question_service_v3.model.question;
import com.pk.question_service_v3.model.questionWrapper;
import com.pk.question_service_v3.model.userResponse;
import com.pk.question_service_v3.service.questionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private questionService qs;

    @RateLimiter(name="question-service",fallbackMethod = "fallbackMethodRateLimiting")
    @CircuitBreaker(name="question-service",fallbackMethod="fallbackMethodCircuitBreaker")
    @GetMapping("allquestions")
    public ResponseEntity<List<question>> getAllQuestions() {
        return new ResponseEntity<>(qs.getQuestions(), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<question> addQuestion(@RequestBody question q) {
        return new ResponseEntity<>(qs.add(q), HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<question> updateQuestion(@RequestBody question q) {
        return new ResponseEntity<>(qs.update(q), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Integer> deleteQuestion(@PathVariable int id) {
        qs.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("generate")
    public ResponseEntity<List<Integer>> generateQuestions(@RequestParam String category, @RequestParam Integer numq) {
        return new ResponseEntity<>(qs.generateQuestions(category, numq), HttpStatus.OK);
    }

    @PostMapping("get")
    public ResponseEntity<List<questionWrapper>> getQuestionsByIds(@RequestBody List<Integer> qids) {
        return new ResponseEntity<>(qs.getQuestionsByIds(qids), HttpStatus.OK);
    }

    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<userResponse> ur)
    {
        return new ResponseEntity<>(qs.getScore(ur),HttpStatus.OK);
    }

    public ResponseEntity<?> fallbackMethodRateLimiting(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too Many Requests nigga try after sometime");
    }

    public ResponseEntity<?> fallbackMethodCircuitBreaker(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Service is not available right now try after some time");
    }
}