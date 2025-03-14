package spring.myproject.controller.fail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import spring.myproject.dto.response.fail.CloseResponse;
import spring.myproject.service.fail.FailService;

@RestController
@RequiredArgsConstructor
public class FailController {

    private final FailService failService;

    @GetMapping(value = "/subscribe/{clientId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable String clientId,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return failService.subscribe(clientId, lastEventId);
    }

    @DeleteMapping(value = "/close/{clientId}")
    public ResponseEntity<CloseResponse> close(@PathVariable String clientId){
        CloseResponse closeResponse = failService.close(clientId);
        return new ResponseEntity<>(closeResponse, HttpStatus.OK);
    }
}
