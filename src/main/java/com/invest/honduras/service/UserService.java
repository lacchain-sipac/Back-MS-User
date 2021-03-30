package com.invest.honduras.service;

import java.util.List;

import com.everis.blockchain.honduras.model.JwtPayload;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.http.request.UserCreateRequest;
import com.invest.honduras.http.request.UserPasswordUpdateRequest;
import com.invest.honduras.http.request.UserStateUpdateRequest;
import com.invest.honduras.http.request.UserUpdateRequest;
import com.invest.honduras.http.response.UserDidDTO;
import com.invest.honduras.http.response.UserItemDTO;
import com.invest.honduras.http.response.UserProfileDTO;
import com.invest.honduras.http.response.UserTokenDTO;

import reactor.core.publisher.Mono;

public interface UserService {

	Mono<List<UserItemDTO>> findAllUser();
	
	Mono<List<String>> findCompany();

	Mono<UserItemDTO> createUser(UserCreateRequest userRequest, String token) throws Exception;

	Mono<UserItemDTO> findByIdUser(String id);
	
	Mono<UserTokenDTO> getToken(String id);

	Mono<UserProfileDTO> getProfileUser(String id);

	Mono<UserItemDTO> updateUser(String id, UserUpdateRequest userRequest, String token);

	Mono<User> findByEmail(String email);

	Mono<User> updatePasswordUser(String id, UserPasswordUpdateRequest userRequest, String token);

	Mono<User> updateStateUser(String id, UserStateUpdateRequest userStateUpdateRequest, String token);

	Mono<UserDidDTO> updateDidUser(String id, JwtPayload jwtPayload);
}
