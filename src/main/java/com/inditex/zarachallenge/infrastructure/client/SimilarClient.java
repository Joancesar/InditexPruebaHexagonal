package com.inditex.zarachallenge.infrastructure.client;

import com.inditex.zarachallenge.application.port.out.SimilarClientPort;
import com.inditex.zarachallenge.infrastructure.exception.SimilarProductsUnexpectedException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Component
public class SimilarClient implements SimilarClientPort {

    private final WebClient similarWebClient;

    public SimilarClient(WebClient similarWebClient) {
        this.similarWebClient = similarWebClient;
    }

    public List<Integer> getSimilarProducts(long productId) {
        return similarWebClient.get()
                .uri("/product/{productId}/similarids", productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                    Mono.error(new SimilarProductsUnexpectedException("Unexpected exception ocurred " +
                                    "in remote server productId: " + productId))
                )
                .bodyToMono(Integer[].class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(SimilarProductsUnexpectedException.class::isInstance)
                )
                .map(Arrays::stream)
                .map(Stream::toList)
                .defaultIfEmpty(Collections.emptyList())
                .block();
    }
}
