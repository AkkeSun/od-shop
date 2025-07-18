package com.productagent.adapter.in;

import com.productagent.application.port.in.UpdateElasticSearchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final UpdateElasticSearchUseCase useCase;

    @GetMapping("/test")
    void test() {
        useCase.update();
    }
}
