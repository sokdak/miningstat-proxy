package me.sokdak.miningstatproxy.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class SimpleClient {
  private final RestTemplate restTemplate;

  public final <B, R> R sendPost(URI uri, @Nullable B body, Class<R> responseType) {
    HttpEntity<B> request = newHttpEntity(body);
    return sendWithLog(uri, HttpMethod.POST, request, responseType);
  }

  public final <R> R sendGet(URI uri, Class<R> responseType) {
    HttpEntity<Void> request = newHttpEntity(null);
    return sendWithLog(uri, HttpMethod.GET, request, responseType);
  }

  public final <B, R> R sendPut(URI uri, @Nullable B body, Class<R> responseType) {
    HttpEntity<B> request = newHttpEntity(body);
    return sendWithLog(uri, HttpMethod.PUT, request, responseType);
  }

  public final void sendDelete(URI uri) {
    HttpEntity<Void> request = newHttpEntity(null);
    sendWithLog(uri, HttpMethod.DELETE, request, Void.class);
  }

  protected <B> HttpEntity<B> newHttpEntity(@Nullable B body) {
    return new HttpEntity<>(body, null);
  }

  private <B, R> R sendWithLog(
      URI uri, HttpMethod httpMethod, HttpEntity<B> request, Class<R> responseType) {
    return restTemplate.exchange(uri, httpMethod, request, responseType).getBody();
  }
}
