package dev.goudie.isometric_notifications;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
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
            @RequestParam String user_id,
            @RequestParam long timeout_in_milliseconds
    ) {
        handler.queueNotification(
                user_id,
                timeout_in_milliseconds
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
    public byte[] getVapidPublicKey(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader(
                "cache-control",
                "s-maxage=1, stale-while-revalidate=599"
        );
        return handler.getVapidPublicKey();
    }
}
