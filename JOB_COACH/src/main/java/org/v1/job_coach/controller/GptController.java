
package org.v1.job_coach.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.v1.job_coach.service.chat.GptService;

@RestController
@Tag(name = "Ai", description = "Consulting Ai 테스트 API")
@RequestMapping("/api/v1/ai-test")
public class GptController {

    private final GptService gptService;

    @Autowired
    public GptController(GptService gptService) {
        this.gptService = gptService;
    }

    @PostMapping("/")
    public ResponseEntity<?> getAssistantMsg(@RequestParam String msg) throws Exception {
        return gptService.getAssistantMsg(msg);
    }

}
