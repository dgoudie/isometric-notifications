package dev.goudie.isometric_notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
public class PushHandler {
    private final Repository repository;
    private final ObjectMapper objectMapper;
    private final PushService pushService;

    public PushHandler(Repository repository,
                       ObjectMapper objectMapper,
                       PushService pushService) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.pushService = pushService;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    void sendNotification(String userId) {
        try {
            String userSubscriptionBase64 = repository.getUserSubscription(userId);
            byte[] userSubscriptionByteArray = Base64
                    .getDecoder()
                    .decode(userSubscriptionBase64);
            String userSubscriptionJson = new String(userSubscriptionByteArray);

            Subscription subscription = objectMapper.readValue(
                    userSubscriptionJson,
                    Subscription.class
            );
            Notification notification = new Notification(
                    subscription,
                    "hello"
            );
            pushService.send(notification);
        } catch (Exception e) {
            log.error(
                    "Failed to send notification",
                    e
            );
        }
    }
}
