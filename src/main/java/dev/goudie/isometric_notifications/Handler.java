package dev.goudie.isometric_notifications;

import com.pusher.pushnotifications.PushNotifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class Handler {
    private static final Long BREAK_TIME = Duration
            .of(
                    2,
                    ChronoUnit.MINUTES
            )
            .toMillis();
    private static final Map<String, ScheduledFuture<?>> MAP = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService;
    private final PushNotifications pushNotifications;

    public Handler(ScheduledExecutorService scheduledExecutorService,
                   PushNotifications pushNotifications) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.pushNotifications = pushNotifications;
    }

    public void queueNotification(
            String userId
    ) {
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(
                () -> sendNotification(userId),
                BREAK_TIME,
                TimeUnit.MILLISECONDS
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
        Map<String, Map> publishRequest = new HashMap();

        Map<String, String> apsAlert = new HashMap();
        apsAlert.put(
                "title",
                "hello"
        );
        apsAlert.put(
                "body",
                "Hello world"
        );
        Map<String, Map> alert = new HashMap();
        alert.put(
                "alert",
                apsAlert
        );
        Map<String, Map> aps = new HashMap();
        aps.put(
                "aps",
                alert
        );
        publishRequest.put(
                "apns",
                aps
        );

        Map<String, String> fcmNotification = new HashMap();
        fcmNotification.put(
                "title",
                "hello"
        );
        fcmNotification.put(
                "body",
                "Hello world"
        );
        Map<String, Map> fcm = new HashMap();
        fcm.put(
                "notification",
                fcmNotification
        );
        publishRequest.put(
                "fcm",
                fcm
        );

        Map<String, String> webNotification = new HashMap();
        webNotification.put(
                "title",
                "hello"
        );
        webNotification.put(
                "body",
                "Hello world"
        );
        Map<String, Map> web = new HashMap();
        web.put(
                "notification",
                webNotification
        );
        publishRequest.put(
                "web",
                web
        );

        try {
            pushNotifications.publishToUsers(
                    Collections.singletonList(userId),
                    publishRequest
            );
        } catch (Exception e) {
            log.error(
                    "And error occurred sending notifications",
                    e
            );
        }
    }
}
