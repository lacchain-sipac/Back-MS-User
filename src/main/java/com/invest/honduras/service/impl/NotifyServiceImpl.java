package com.invest.honduras.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invest.honduras.client.NotifyClient;
import com.invest.honduras.http.client.ItemUserNotifyRequest;
import com.invest.honduras.http.client.UserNotifyRequest;
import com.invest.honduras.service.NotifyService;

import reactor.core.publisher.Mono;

@Service
public class NotifyServiceImpl implements NotifyService{

	
	@Autowired
	NotifyClient notifyClient;
	
	@Override
	public Mono<Boolean> sendUserNotify(ItemUserNotifyRequest userToNotifyRequest) {
		return notifyClient.sendUserNotify(userToNotifyRequest);
	}
	@Override
	public Mono<Boolean> sendUpdateUserNotify(UserNotifyRequest userUpdateRequest) {
		return notifyClient.sendUpdateNotify(userUpdateRequest);
	}
	


}
