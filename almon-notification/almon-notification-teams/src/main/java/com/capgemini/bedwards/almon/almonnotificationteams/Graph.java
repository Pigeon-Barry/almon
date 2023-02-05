package com.capgemini.bedwards.almon.almonnotificationteams;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.SneakyThrows;
import okhttp3.Request;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class Graph {
    private static String authority;
    private static String clientId;
    private static String secret;
    private static String scope;
    private static ConfidentialClientApplication app;

    @SneakyThrows
    public Graph(NotificationTeamsConfig config) {
        authority = config.getAuthority();
        clientId = config.getClientId();
        secret = config.getClientSecret();
        scope = config.getClientScope();
        app = ConfidentialClientApplication.builder(
                        clientId,
                        ClientCredentialFactory.createFromSecret(secret))
                .authority(authority)
                .build();
    }

    public GraphServiceClient<Request> getGraphClient() {
        return GraphServiceClient
                .builder()
                .authenticationProvider(url -> {
                    ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                                    Collections.singleton(scope))
                            .build();
                    CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);

                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            return future.get().accessToken();
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    });
                })
                .buildClient();
    }
}
