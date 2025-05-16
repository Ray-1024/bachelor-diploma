package ray1024.submissiontestingworker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/ping")
public class PingController {
    @GetMapping
    String ping() {
        return "pong";
    }
}
