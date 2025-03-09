package spring.myproject.emitter.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void deleteById(String emitterId);
    SseEmitter findById(String clientId);
}
