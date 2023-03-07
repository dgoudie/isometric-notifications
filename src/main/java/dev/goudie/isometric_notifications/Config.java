package dev.goudie.isometric_notifications;

import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Slf4j
public class Config {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(10);
    }

    @Bean
    public PublicKey publicKey(@Value("${vapid.public.key}") String vapidPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        return Utils.loadPublicKey(vapidPublicKey);
    }

    @Bean
    public PushService pushService(
            @Value("${vapid.public.key}") String vapidPublicKey,
            @Value("${vapid.private.key}") String vapidPrivatekey,
            @Value("${jwt.subject}") String subject
    ) throws GeneralSecurityException {
        return new PushService(
                vapidPublicKey,
                vapidPrivatekey,
                subject
        );
    }
}
