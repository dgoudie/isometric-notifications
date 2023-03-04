package dev.goudie.isometric_notifications;

import dev.goudie.isometric_notifications.interceptor.PusherBeamsInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Slf4j
public class Config {

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(10);
    }

    @Bean
    public PusherBeamsInterceptor pusherBeamsInterceptor(
            @Value("${pusher.beams.secret}") String secret
    ) {
        return new PusherBeamsInterceptor(secret);
    }

    @Bean
    public RestTemplate pusherBeamsApiClient(
            @Value("${pusher.beams.instance.id}") String instanceId,
            PusherBeamsInterceptor pusherBeamsInterceptor
    ) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(
                new DefaultUriBuilderFactory("https://%s.pushnotifications.pusher.com/publish_api/v1/instances/%s/publishes/users".formatted(
                        instanceId,
                        instanceId
                ))
        );
        restTemplate.setInterceptors(
                Collections.singletonList(pusherBeamsInterceptor)
        );
        return restTemplate;
    }
}
