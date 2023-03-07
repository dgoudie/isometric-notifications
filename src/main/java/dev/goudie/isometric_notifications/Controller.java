package dev.goudie.isometric_notifications;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final Handler handler;

    public Controller(Handler handler) {
        this.handler = handler;
    }

    @PostMapping(path = "queue_notification")
    public void queueNotification(
            @RequestParam String user_id
    ) {
        handler.queueNotification(
                user_id
        );
    }

    @PostMapping(path = "clear_notification")
    public void clearNotification(
            @RequestParam String user_id
    ) {
        handler.clearNotification(
                user_id
        );
    }

    @GetMapping(path = "vapid_public_key", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getVapidPublicKey() {
        return handler.getVapidPublicKey();
    }
}
