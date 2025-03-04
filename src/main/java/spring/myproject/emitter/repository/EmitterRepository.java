package spring.myproject.emitter.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);


    Map<String, SseEmitter> findAllEmitterByUserId(String userId);

    void deleteById(String emitterId);


}
