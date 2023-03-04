package dev.goudie.isometric_notifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class Handler {
    private static final Map<String, ScheduledFuture<?>> MAP = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService;
    private final RestTemplate pusherBeamsApiClient;
    private final Long breakTimeInSeconds;

    public Handler(ScheduledExecutorService scheduledExecutorService,
                   RestTemplate pusherBeamsApiClient,
                   @Value("${break.time.in.seconds}") Long breakTimeInSeconds) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.pusherBeamsApiClient = pusherBeamsApiClient;
        this.breakTimeInSeconds = breakTimeInSeconds;
    }


    public void queueNotification(
            String userId
    ) {
        clearNotification(userId);
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(
                () -> sendNotification(userId),
                breakTimeInSeconds,
                TimeUnit.SECONDS
        );
        MAP.put(
                userId,
                scheduledFuture
        );
    }

    public void clearNotification(String userId) {
        ScheduledFuture<?> scheduledFutureOrNull = MAP.getOrDefault(
                userId,
                null
        );
        if (scheduledFutureOrNull != null) {
            scheduledFutureOrNull.cancel(false);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void sendNotification(String userId) {
        String jsonPayload = """
                {
                  "users": ["%s"],
                  "web": {
                    "notification": {
                      "action": "Back to Workout",
                      "body": "Time is up!",
                      "icon": "https://isometric.goudie.dev/images/isometric.png",
                      "hide_notification_if_site_has_focus": true,
                      "vibrate": [200, 100, 200]
                    }
                  }
                }
                """.formatted(userId);
        pusherBeamsApiClient.postForEntity(
                "",
                jsonPayload,
                Void.class
        );
    }
}
