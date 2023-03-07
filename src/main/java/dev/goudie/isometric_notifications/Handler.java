package dev.goudie.isometric_notifications;

import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Utils;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
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
    private final PushHandler pushHandler;
    private PublicKey publicKey;

    public Handler(ScheduledExecutorService scheduledExecutorService,
                   PushHandler pushHandler,
                   PublicKey publicKey) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.pushHandler = pushHandler;
        this.publicKey = publicKey;
    }


    public void queueNotification(
            String userId,
            long timeoutInMilliseconds) {
        clearNotification(userId);
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(
                () -> pushHandler.sendNotification(userId),
                timeoutInMilliseconds,
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

    public byte[] getVapidPublicKey() {
        return Utils.encode((ECPublicKey) publicKey);
    }


}
