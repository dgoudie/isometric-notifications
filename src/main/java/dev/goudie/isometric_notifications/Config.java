package dev.goudie.isometric_notifications;

import com.pusher.pushnotifications.PushNotifications;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class Config {

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(10);
    }

    @Bean
    public PushNotifications pushNotifications(
            @Value("${pusher.beams.instance.id}") String instanceId,
            @Value("${pusher.beams.secret}") String secret
    ) {
        return new PushNotifications(
                instanceId,
                secret
        );
    }
}
