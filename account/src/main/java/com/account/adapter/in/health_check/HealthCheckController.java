package com.account.adapter.in.health_check;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthCheckController {

    @GetMapping("/startup")
    public String startUp() {
        return "startup!";
    }

    @GetMapping("/readiness")
    public String readiness() {
        return "readiness";
    }

    @GetMapping("/liveness")
    public String liveness() {
        return "liveness";
    }
}
