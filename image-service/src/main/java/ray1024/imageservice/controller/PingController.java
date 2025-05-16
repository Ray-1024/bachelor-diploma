package ray1024.imageservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/ping")
public class PingController {

    @GetMapping
    public String ping() {
        return "pong";
    }
}
