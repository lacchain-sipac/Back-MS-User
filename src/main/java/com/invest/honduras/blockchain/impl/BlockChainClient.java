package com.invest.honduras.blockchain.impl;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.invest.honduras.dao.QueueDao;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.model.QueueTransaction;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserQueue;
import com.invest.honduras.domain.model.UserRoleQueue;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.request.RoleRequest;
import com.invest.honduras.http.request.UserBlockchainRequest;
import com.invest.honduras.http.request.UserCapRequest;
import com.invest.honduras.http.request.UserUpdateBlockchainRequest;
import com.invest.honduras.http.response.ResponseGeneral;
import com.invest.honduras.util.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@Slf4j
public class BlockChainClient {

	private final WebClient webClient;

	@Autowired
	UserDao userDao;

	@Autowired
	QueueDao queueDao;
	
	public BlockChainClient() {
		this.webClient = WebClient.builder().baseUrl(Constant.HOST_BLOCKCHAIN)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	
	
	
	public void addUser(UserBlockchainRequest request) {

		this.webClient.post().uri(Constant.API_URL_BLOCKCHAIN_USER)
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(request), UserBlockchainRequest.class)
				.retrieve()
				.bodyToMono(ResponseGeneral.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
				.doOnError(e -> {
					log.error("ERROR.webClient.addUser: ", e);
					addUserDB(request);
					throw new GlobalException(HttpStatus.SERVICE_UNAVAILABLE, "API_URL_BLOCKCHAIN_USER:" + e.getMessage());
				}).subscribe(data -> {
					log.info("RESULT.addUser:>" + new Gson().toJson(data));
				});

	}

	public void updateUser(UserUpdateBlockchainRequest request) {

		this.webClient.put().uri(Constant.API_URL_BLOCKCHAIN_USER)
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(request), UserUpdateBlockchainRequest.class)
				.retrieve()
				.bodyToMono(ResponseGeneral.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
				.doOnError(e -> {
					log.error("ERROR.webClient. updateUser: ", e);
					updateUserDB(request) ;
					throw new GlobalException(HttpStatus.SERVICE_UNAVAILABLE, "API_URL_BLOCKCHAIN_USER:"+ e.getMessage());
				}).subscribe(data -> {
					log.info("RESULT.updateUser:>" + new Gson().toJson(data));
				});

	}

	public Mono<ResponseGeneral> setCap(UserCapRequest request) {

		return this.webClient.put().uri(Constant.API_URL_BLOCKCHAIN_USER_CAP).accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

				.body(Mono.just(request), UserCapRequest.class)
				.retrieve().
				bodyToMono(ResponseGeneral.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
				.doOnError(e -> {
					log.error("ERROR.webClient. setCap: ", e);
					throw new GlobalException(HttpStatus.SERVICE_UNAVAILABLE, "API_URL_BLOCKCHAIN_USER_CAP:"+ e.getMessage());
				}).flatMap(data -> {
					log.info("RESULT.setCap:>" + new Gson().toJson(data));
					return Mono.just(data);
				});
	}
	
	
	private void addUserDB( UserBlockchainRequest request) {
		 userDao.findByEmail(request.getEmailUser()).map(user -> {

			insertQueueUserDB(request.getProxyAddressAdministrator(), request.getEmailUser(), user.getRoles());

			return user;
		}).subscribe();
	}
	
	private void insertQueueUserDB(String proxyAdministrator, String emailUser, List<RoleRequest> roles) {
		//log.info("insertQueueUser {},{},{}: ", proxyAdministrator, emailUser, new Gson().toJson(roles));
		UserQueue userQueu = new UserQueue();
		userQueu.setEmailUser(emailUser);
		userQueu.setProxyAddressAdministrator(proxyAdministrator);
		userQueu.setRoles(roles);

		QueueTransaction tx = new QueueTransaction();
		tx.setUser(userQueu);

		queueDao.addTransaction(tx);
	}
	
	private void updateUserDB(UserUpdateBlockchainRequest request) {
		 userDao.findByEmail(request.getEmailUser()).map(user -> {

			log.info("updateUser exists? user:{} ", new Gson().toJson(user));

			if (StringUtils.isEmpty(user.getProxyAddress())) {

				insertQueueUserDB(request.getProxyAddressAdministrator(), request.getEmailUser(), user.getRoles());

			} else {
				//user.setRolesOld(request.getOldRole());

				updateUserBlockChainDb(request.getProxyAddressAdministrator(), user, request.getOldRole());

			}

			return user;

		}).subscribe();
	}
	
	
	public void updateUserBlockChainDb(String proxyAdministrator, User userItem, List<RoleRequest> listRoleOld) {

//		List<Role> listRoleOld = userItem.getRolesOld();
//		
//		ListUtil.removeSimilarRole(listRoleOld, listRoleNew);
//
//		if (listRoleOld != null) {
//			listRoleOld.forEach(role -> {
//
//				insertErrorQueueUserRole( proxyAdministrator, userItem.getProxyAddress(), role.getCode(), false);
//
//			});
//		}

//		if (listRoleNew != null) { 
		log.info("updateUserBlockChain.proxyAdministrator {}, {}", proxyAdministrator, new Gson().toJson(userItem.getRoles()));

		for (RoleRequest role : userItem.getRoles()) {
			log.info("user:{}, role:{}", userItem.getProxyAddress(), role.getCode());
			insertErrorQueueUserRole(proxyAdministrator, userItem.getProxyAddress(), role.getCode(), true);

		}
//		}

	}
	
	private void insertErrorQueueUserRole(String proxyAdministrator, String proxyUser, String role, boolean insert) {

		UserRoleQueue userQueu = new UserRoleQueue();
		userQueu.setProxyUser(proxyUser);
		userQueu.setProxyAddressAdministrator(proxyAdministrator);
		userQueu.setRole(role);
		userQueu.setInsert(insert);

		QueueTransaction tx = new QueueTransaction();
		tx.setUserRole(userQueu);

		queueDao.addTransaction(tx);
	}

}
