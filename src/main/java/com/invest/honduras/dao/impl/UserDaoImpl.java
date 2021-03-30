package com.invest.honduras.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.invest.honduras.client.RedisClient;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.map.UserMap;
import com.invest.honduras.domain.model.Role;
import com.invest.honduras.domain.model.StatusUser;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserLogin;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.request.RoleRequest;
import com.invest.honduras.http.request.UserCreateRequest;
import com.invest.honduras.http.request.UserPasswordUpdateRequest;
import com.invest.honduras.http.request.UserStateUpdateRequest;
import com.invest.honduras.http.request.UserUpdateRequest;
import com.invest.honduras.repository.UserRepository;
import com.invest.honduras.security.util.PBKDF2Encoder;
import com.invest.honduras.util.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserDaoImpl implements UserDao {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Autowired
	RedisClient redisClient;

	@Override
	public Flux<User> listUser(String field, Sort sort) {

		return userRepository.findByFullnameLikeIgnoreCaseOrEmailLikeIgnoreCase(field, field, sort);

	}

	@Override
	public Flux<User> listAllUser() {
		return userRepository.findAll();

	}

	@Override
	public Mono<User> createUser(UserCreateRequest userRequest, UserLogin userLogin, List<StatusUser> listStateUser,
			List<Role> roles) {
		log.info("UserDaoImpl.createUser : " + userRequest.getEmail());
		User user = new User();
		new UserMap().mapping(userRequest, user, userLogin, listStateUser, roles);

		return userRepository.findByEmail(userRequest.getEmail().toLowerCase()).doOnSuccess(item -> {
			if (item != null) {
				throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_EMAIL_EXIST.name());

			}
		}).switchIfEmpty(userRepository.save(user));

	}

	@Override
	public Mono<User> findByEmail(String email) {
		return userRepository.findByEmail(email);

	}

	@Override
	public Mono<User> findByIdUser(String id) {
		return userRepository.findById(id)
				.switchIfEmpty(Mono.error(new GlobalException(HttpStatus.NOT_FOUND, Constant.MESSAGE_USER_NOT_EXIST)));

	}

	@Override
	public Mono<User> updateUser(String id, UserUpdateRequest userUpdateRequest, UserLogin userLogin,
			List<StatusUser> listStateUser) {

		Mono<User> requestCredentialMono = findByIdUser(id).doOnSuccess(findUser -> {

			if (findUser == null) {
				throw new GlobalException(HttpStatus.NOT_FOUND, Constant.MESSAGE_USER_NOT_EXIST);
			} else {

				List<RoleRequest> rolesOld = findUser.getRoles();

				new UserMap().mapping(userUpdateRequest, findUser, userLogin, listStateUser);
				userRepository.save(findUser).subscribe();

				findUser.setRolesOld(rolesOld);

				findUser.getRolesOld().forEach(i -> log.info("old-->" + i.getCode()));
			}
		});
		return requestCredentialMono;
	}

	@Override
	public Flux<User> findUserByRol(String rol) {
		return userRepository.findUserByRol(rol);
	}

	@Override
	public void updateUser(User user) {

		userRepository.save(user).subscribe();

	}

	@Override
	public Mono<User> updatePasswordUser(String id, UserPasswordUpdateRequest userRequest, UserLogin userLogin,
			List<StatusUser> listStateUser) {

		Mono<User> requestCredentialMono = findByIdUser(id).doOnSuccess(findUser -> {

			if (findUser == null) {
				throw new GlobalException(HttpStatus.NOT_FOUND, Constant.MESSAGE_USER_NOT_EXIST);
			}
			if (findUser.getEmail().equalsIgnoreCase(userRequest.getPassword().trim())) {
				throw new GlobalException(HttpStatus.BAD_REQUEST,
						TypeStatusCode.MESSAGE_USER_EMAIL_EQUAL_PASSWORD.name());

			}
			String password = passwordEncoder.encode(userRequest.getPassword().trim());

			userRequest.setPassword(password);

			log.info("userLogin :" + userLogin.getUserLogin());
			if (!(findUser.getEmail().equals(userLogin.getUserLogin()))) {
				throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.MESSAGE_USER_EMAIL_NOT_PERMIT.name());

			}
			new UserMap().mappingPassword(userRequest, findUser, userLogin, listStateUser);

			userRepository.save(findUser).subscribe();
		});
		return requestCredentialMono;

	}

	@Override
	public Mono<User> updateDidUser(String id, String did, UserLogin userLogin) {

		Mono<User> requestCredentialMono = findByIdUser(id).doOnSuccess(findUser -> {

			if (findUser == null) {
				throw new GlobalException(HttpStatus.NOT_FOUND, Constant.MESSAGE_USER_NOT_EXIST);
			}

			log.info("userLogin :" + userLogin.getUserLogin());
			if (!(findUser.getEmail().equals(userLogin.getUserLogin()))) {
				throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.MESSAGE_USER_EMAIL_NOT_PERMIT.name());
			}
			new UserMap().mappingUpdateDid(did, findUser, userLogin);

			userRepository.save(findUser).subscribe();
		});
		return requestCredentialMono;

	}

	@Override
	public Mono<User> updateStateUser(String id, UserStateUpdateRequest userStateUpdateRequest, UserLogin userLogin,
			List<StatusUser> listStateUser) {
		// TODO Auto-generated method stub
		Mono<User> requestCredentialMono = findByIdUser(id).doOnSuccess(findUser -> {

			if (findUser == null) {
				throw new GlobalException(HttpStatus.NOT_FOUND, Constant.MESSAGE_USER_NOT_EXIST);
			} else {

				new UserMap().mapping(userStateUpdateRequest, findUser, userLogin, listStateUser);
				if (Constant.USER_CODE_DISABLED.equals(findUser.getCodeStatus())) {

					String keyValue = Constant.SESSION + findUser.getId();
					if (redisClient.existSession(keyValue)) {
						redisClient.closeSession(keyValue);

					}

				}

				userRepository.save(findUser).subscribe();
			}
		});
		return requestCredentialMono;
	}

}
