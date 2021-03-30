package com.invest.honduras.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.invest.honduras.http.client.ItemUserNotifyRequest;
import com.invest.honduras.http.client.UserNotifyRequest;
import com.invest.honduras.util.Constant;

import reactor.core.publisher.Mono;

@Component
public class NotifyClient {

	private final WebClient webClient;

	public NotifyClient() {
		this.webClient = WebClient.builder().baseUrl(Constant.HOST_NOTIFY)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	public Mono<Boolean> sendUserNotify(ItemUserNotifyRequest itemUserNotifyRequest) {

		this.webClient.post().uri(Constant.API_URL_USERMAIL).accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(itemUserNotifyRequest), ItemUserNotifyRequest.class).exchange().doOnError(e -> {
					System.out.println("doOnError: " + e.getMessage());
					throw new RuntimeException();
				}

				).subscribe(data -> {
					System.out.println("enviando correo--->>>>" + data.statusCode());
				});

		return Mono.just(Boolean.TRUE);
	}	
	
	public Mono<Boolean> sendUpdateNotify(UserNotifyRequest  userNotifyRequest) {

		this.webClient.post().uri(Constant.API_URL_COMPLETE_PERFIL).accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(userNotifyRequest), UserNotifyRequest.class).exchange().doOnError(e -> {
					System.out.println("doOnError: " + e.getMessage());
					throw new RuntimeException();
				}).subscribe(data -> {
					System.out.println("enviando correo--->>>>" + data.statusCode());
				});

		return Mono.just(Boolean.TRUE);
	}
	
	
	public Mono<Boolean> sendEmail(UserNotifyRequest  userNotifyRequest) {

		this.webClient.post().uri(Constant.API_URL_COMPLETE_PERFIL).accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(userNotifyRequest), UserNotifyRequest.class).exchange().doOnError(e -> {
					System.out.println("doOnError: " + e.getMessage());
					throw new RuntimeException();
				}).subscribe(data -> {
					System.out.println("enviando correo--->>>>" + data.statusCode());
				});

		return Mono.just(Boolean.TRUE);
	}

}
