package ray1024.taskexecutorservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/ping")
public class PingController {
    @GetMapping
    String ping() {
        return "pong";
    }
}
