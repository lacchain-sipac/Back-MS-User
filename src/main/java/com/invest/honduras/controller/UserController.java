package com.invest.honduras.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.everis.blockchain.honduras.model.JwtPayload;
import com.everis.blockchain.honduras.util.JwtExpirationException;
import com.everis.blockchain.honduras.util.JwtVerifyException;
import com.everis.blockchain.honduras.util.Secp256k1Utils;
import com.invest.honduras.config.SessionTokenJWT;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.request.UserCreateRequest;
import com.invest.honduras.http.request.UserForgetPasswordRequest;
import com.invest.honduras.http.request.UserPasswordUpdateRequest;
import com.invest.honduras.http.request.UserStateUpdateRequest;
import com.invest.honduras.http.request.UserUpdateRequest;
import com.invest.honduras.http.response.CompanyFindResponse;
import com.invest.honduras.http.response.UserDidResponse;
import com.invest.honduras.http.response.UserFindAllResponse;
import com.invest.honduras.http.response.UserGeneralResponse;
import com.invest.honduras.http.response.UserProfileResponse;
import com.invest.honduras.http.response.UserResponse;
import com.invest.honduras.http.response.UserTokenResponse;
import com.invest.honduras.service.UserService;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Otp;
import com.invest.honduras.util.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController implements ApiUserController {

	@Autowired
	UserService userService;

	@GetMapping(value = "/find-all", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<UserFindAllResponse>> findAllUser() {

		log.info("********** Inicio [UserController][findAllUser] ******* ");
		return userService.findAllUser().flatMap(
				userPage -> Mono.just(ResponseEntity.ok(new UserFindAllResponse(HttpStatus.OK.value(), userPage))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/find-company", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<CompanyFindResponse>> findCompany() {
		log.info("********** Inicio [UserController][findCompany] ******* ");
		return userService.findCompany()
				.flatMap(list -> Mono.just(ResponseEntity.ok(new CompanyFindResponse(HttpStatus.OK.value(), list))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ResponseEntity<UserResponse>> addUser(@RequestHeader("Authorization") String token,
			@Valid @RequestBody UserCreateRequest userRequest) throws Exception {

		log.info("********** Inicio [UserController][addUser] ******* ");

		if (StringUtils.isEmpty(userRequest.getEmail())) {

			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_EMAIL_EMPTY.name());
		}

		if (userRequest.getRoles() == null || (userRequest.getRoles() != null && userRequest.getRoles().size() <= 0)) {

			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_ROLE_EMPTY.name());
		}
		
		if (StringUtils.isEmpty(userRequest.getFullname()) || StringUtils.isEmpty(userRequest.getSurnames())) {

			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_NAME_EMPTY.name());
		}
		return userService.createUser(userRequest, token)
				.map(data -> ResponseEntity.ok(new UserResponse(HttpStatus.OK.value(), data)));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<UserResponse>> updateUser(String id, UserUpdateRequest user, String token) {

		log.info("********** Inicio [UserController][updateUser] ******* ");
		if (user.getRoles() == null || (user.getRoles() != null && user.getRoles().size() <= 0)) {
			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_ROLE_EMPTY.name());
		}

		if (StringUtils.isEmpty(user.getFullname()) || StringUtils.isEmpty(user.getSurnames())) {
			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_NAME_EMPTY.name());
		}

		return userService.updateUser(id, user, token)
				.map(data -> ResponseEntity.ok(new UserResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<UserResponse>> findByIdUser(@PathVariable("id") String id) {

		log.info("********** Inicio [UserController][findByIdUser] ******* ");

		return userService.findByIdUser(id)
				.map(data -> ResponseEntity.ok(new UserResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/profile/{id}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<UserProfileResponse>> getProfile(@PathVariable("id") String id) {

		log.info("********** Inicio [UserController][getProfile] ******* ");
		return userService.getProfileUser(id)
				.map(data -> ResponseEntity.ok(new UserProfileResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/state/{id}")
	public Mono<ResponseEntity<UserGeneralResponse>> updateStateUser(@PathVariable(value = "id") String id,
			@Valid @RequestBody UserStateUpdateRequest userStateUpdateRequest,
			@RequestHeader("Authorization") String token) {

		log.info("********** Inicio [UserController][updateStateUser] ******* ");

		return userService.updateStateUser(id, userStateUpdateRequest, token)
				.map(data -> ResponseEntity.ok(new UserGeneralResponse(HttpStatus.OK.value(), null)));

	}

	@PutMapping("/password/{id}")
	@Override
	public Mono<ResponseEntity<UserGeneralResponse>> updatePasswordUser(String id,
			UserPasswordUpdateRequest userPasswordUpdateRequest, String token) {

		String password = userPasswordUpdateRequest.getPassword();
		String confirmPassword = userPasswordUpdateRequest.getConfirmPassword();
		String number = userPasswordUpdateRequest.getVerifiedCode();
		String secretkey = userPasswordUpdateRequest.getSecretKey();
		boolean dfa = userPasswordUpdateRequest.isEs2FA();
		log.info("********** Inicio [UserController][updatePasswordUser] ******* ");
		if (userPasswordUpdateRequest.getType() == null || userPasswordUpdateRequest.getType().isEmpty()) {

			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.MESSAGE_USER_TYPE_EMPTY.name());

		}
		if (!(Constant.TYPE_CHANGE_PASSWORD.equals(userPasswordUpdateRequest.getType())
				|| Constant.TYPE_COMPLETE_PASSWORD.equals(userPasswordUpdateRequest.getType()))) {

			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.MESSAGE_USER_TYPE_EMPTY.name());

		}

		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPassword)) {

			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.MESSAGE_USER_PASSWORD_EMPTY.name());

		}

		if (!(Util.match(password, Constant.REGEX))) {

			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.MESSAGE_USER_PASSWORD_REGEX.name());

		}

		if (dfa) {
			if (!(Otp.verificationCode2FA(number, secretkey))) {

				throw new GlobalException(HttpStatus.BAD_REQUEST,
						TypeStatusCode.MESSAGE_USER_INVALID_VERIFICATION_CODE_2FA.name());

			}
		}

		if (!password.equals(confirmPassword)) {

			throw new GlobalException(HttpStatus.BAD_REQUEST,
					TypeStatusCode.MESSAGE_USER_PASSWORD_NOT_EQUAL.getMessage());

		} else {
			return userService.updatePasswordUser(id, userPasswordUpdateRequest, token)
					.map(data -> ResponseEntity.ok(new UserGeneralResponse(HttpStatus.OK.value(), null)));
		}

	}

	@PostMapping("/forget-password/")
	@Override
	public Mono<ResponseEntity<UserGeneralResponse>> forgetPasswordUser(
			UserForgetPasswordRequest userForgetPasswordRequest) {

		log.info("********** Inicio [UserController][forgetPasswordUser] ******* ");

		return userService.findByEmail(userForgetPasswordRequest.getEmail())
				.map(data -> ResponseEntity.ok(new UserGeneralResponse(HttpStatus.OK.value(), "Provicional")))
				.defaultIfEmpty(ResponseEntity.ok(new UserGeneralResponse(HttpStatus.OK.value(), "Provicional")));
	}

	@GetMapping(value = "/did/token/", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<UserTokenResponse>> getToken(@RequestHeader("Authorization") String token) {
		String email = Util.decodeToken(token);
		return userService.getToken(email)
				.map(data -> ResponseEntity.ok(new UserTokenResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@Override
	@GetMapping(value = "/did/register-did", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<UserDidResponse>> updateDidUserJWT(
			@RequestParam(value = "access_token") String accessToken, @RequestParam(value = "state") String state) {

		log.info("********** updateDidUserJWT.access_token : " + accessToken);

		JwtPayload jwtPayload = null;

		try {
			jwtPayload = Secp256k1Utils.verifyJWTokenSecp256k1(accessToken, null);

			log.info("jwtPayload.getSub() == " + jwtPayload.getSub());
			log.info("jwtPayload.getIss() == " + jwtPayload.getIss());

		} catch (JwtExpirationException | JwtVerifyException e) {

			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_JWT_VALIDATE.name());

		} catch (Exception e) {

			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_JWT_VALIDATE.name());

		}

		String email = SessionTokenJWT.getInstance().getValue(state);

		if (StringUtils.isEmpty(email)) {
			log.info("EMAIL_NOT_FOUND");

			throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_STATE_VALIDATE.name());

		}

		return userService.updateDidUser(email, jwtPayload)
				.map(data -> ResponseEntity.ok(new UserDidResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());

	}

}
