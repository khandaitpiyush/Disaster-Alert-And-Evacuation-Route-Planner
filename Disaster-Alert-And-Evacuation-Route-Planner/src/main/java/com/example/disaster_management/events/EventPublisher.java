package com.example.disaster_management.events;

import com.example.disaster_management.model.DisasterAlert;
import com.example.disaster_management.model.DisasterEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class EventPublisher {

    // Thread-safe list of all active emitters (connected clients)
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // Called by /api/stream/events
    public SseEmitter addEmitter() {
        // 0L = no timeout (or you can set a value like 30_000L for 30s)
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        // Clean up on completion/timeout/error
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(emitter);
        });
        emitter.onError(e -> {
            emitter.complete();
            emitters.remove(emitter);
        });

        // Optional: send a small initial event so frontend knows it's connected
        try {
            emitter.send(SseEmitter.event()
                    .name("init")
                    .data("connected"));
        } catch (IOException e) {
            emitter.complete();
            emitters.remove(emitter);
        }

        return emitter;
    }

    // Called from AlertController.createAlert(...)
    public void publishAlert(DisasterEvent event) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("alert")    // frontend listens to "alert"
                                .data(event)      // sent as JSON
                );
            } catch (IOException e) {
                emitter.complete();
                deadEmitters.add(emitter);
            }
        }

        // Remove dead/broken connections
        emitters.removeAll(deadEmitters);
    }

    public void publish(DisasterAlert alertDTO) {
    }
}
