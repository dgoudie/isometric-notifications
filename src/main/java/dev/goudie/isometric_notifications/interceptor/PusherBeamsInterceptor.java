package dev.goudie.isometric_notifications.interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Collections;

public class PusherBeamsInterceptor implements ClientHttpRequestInterceptor {

    private final String secret;
    private static final String sdkVersion = "1.1.1";

    public PusherBeamsInterceptor(String secret) {
        this.secret = secret;
    }

    @NotNull
    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        @NotNull byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        request
                .getHeaders()
                .setContentType(
                        MediaType.APPLICATION_JSON
                );
        request
                .getHeaders()
                .setAccept(
                        Collections.singletonList(MediaType.APPLICATION_JSON)
                );
        request
                .getHeaders()
                .setBearerAuth(secret);
        request
                .getHeaders()
                .set(
                        "X-Pusher-Library",
                        "pusher-push-notifications-server-java %s".formatted(sdkVersion)
                );
        return execution.execute(
                request,
                body
        );
    }
}
