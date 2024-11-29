package com.laoumri.socialmediaspringbackend.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ResponseHeaderInterceptor implements WebGraphQlInterceptor {
    @Value("${jwt.cookieName}")
    private String cookieName;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        return chain.next(request).doOnNext((response) -> {
            String value = response.getExecutionInput().getGraphQLContext().get(cookieName);
            ResponseCookie cookie = ResponseCookie.from(cookieName, value)
                    .maxAge(24 * 60 * 60)
                    .httpOnly(true)
                    .build();
            response.getResponseHeaders().add(HttpHeaders.SET_COOKIE, cookie.toString());
        });
    }
}