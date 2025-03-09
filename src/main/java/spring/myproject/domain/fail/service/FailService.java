package spring.myproject.domain.fail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import spring.myproject.domain.fail.Fail;
import spring.myproject.domain.fail.dto.response.CloseResponse;
import spring.myproject.domain.fail.repostory.FailRepository;
import spring.myproject.domain.user.User;
import spring.myproject.emitter.repository.EmitterRepository;
import spring.myproject.util.ConstClass;

import java.io.IOException;

import static spring.myproject.util.ConstClass.*;

@Service
@RequiredArgsConstructor
@Transactional
public class FailService {

    private final FailRepository failRepository;
    private final EmitterRepository emitterRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(String clientId, String lastEventId) {
        SseEmitter emitter = emitterRepository.save(clientId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.deleteById(clientId));
        emitter.onTimeout(() -> emitterRepository.deleteById(clientId));
        sendToClient(emitter, clientId, "EventStream Created. [clientId=" + clientId + "]");
        return emitter;
    }

    public void send(String clientId){
        SseEmitter sseEmitter = emitterRepository.findById(clientId);
        Fail fail = Fail.of(clientId,"fail send Mail");
        sendToClient(sseEmitter,clientId,fail);
        failRepository.save(fail);
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("connect Fail!");
        }
    }

    public CloseResponse close(String clientId) {
        emitterRepository.deleteById(clientId);
        return CloseResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }
}
