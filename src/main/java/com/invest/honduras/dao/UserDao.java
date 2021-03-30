package com.invest.honduras.dao;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.invest.honduras.domain.model.Role;
import com.invest.honduras.domain.model.StatusUser;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserLogin;
import com.invest.honduras.http.request.UserCreateRequest;
import com.invest.honduras.http.request.UserPasswordUpdateRequest;
import com.invest.honduras.http.request.UserStateUpdateRequest;
import com.invest.honduras.http.request.UserUpdateRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserDao {

	Flux<User> listUser(String field, Sort sort);
	
	Flux<User> listAllUser();
	
	Mono<User> createUser(UserCreateRequest userCreateRequest,UserLogin userLogin, List<StatusUser> listStateUser,List<Role> roles ) ;

	Mono<User> findByIdUser(String id);

	Mono<User> updateUser(String id, UserUpdateRequest userUpdateRequest,UserLogin userLogin, List<StatusUser> listStateUser);
		    
	Flux<User> findUserByRol(String rol);	
	void updateUser(User user);
	
	Mono<User> findByEmail(String email);	
	Mono<User> updatePasswordUser(String id,UserPasswordUpdateRequest userRequest, UserLogin userLogin,List<StatusUser> listStateUser);
	
	Mono<User> updateStateUser(String id, UserStateUpdateRequest userStateUpdateRequest,UserLogin userLogin, List<StatusUser> listStateUser);

	Mono<User> updateDidUser(String id,String did, UserLogin userLogin);
	
}
