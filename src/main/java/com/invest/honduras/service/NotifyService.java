package com.invest.honduras.service;

import com.invest.honduras.http.client.ItemUserNotifyRequest;
import com.invest.honduras.http.client.UserNotifyRequest;

import reactor.core.publisher.Mono;


public interface NotifyService {

	public Mono<Boolean> sendUserNotify(ItemUserNotifyRequest userToNotifyRequest);	
	public Mono<Boolean> sendUpdateUserNotify(UserNotifyRequest userUpdateRequest);

}
