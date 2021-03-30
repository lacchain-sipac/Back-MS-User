package com.invest.honduras.service.impl;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.everis.blockchain.honduras.model.JwtPayload;
import com.everis.blockchain.honduras.util.MnidUtils;
import com.everis.blockchain.honduras.util.Secp256k1Utils;
import com.invest.honduras.blockchain.impl.BlockChainClient;
import com.invest.honduras.config.ApplicationBlockChain;
import com.invest.honduras.config.ConfigurationProject;
import com.invest.honduras.config.SessionTokenJWT;
import com.invest.honduras.dao.RoleDao;
import com.invest.honduras.dao.StatusDao;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.map.UserMap;
import com.invest.honduras.domain.model.Role;
import com.invest.honduras.domain.model.StatusUser;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserLogin;
import com.invest.honduras.enums.EnumMessage;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.ItemUserNotifyRequest;
import com.invest.honduras.http.client.UserNotifyRequest;
import com.invest.honduras.http.request.UserBlockchainRequest;
import com.invest.honduras.http.request.UserCapRequest;
import com.invest.honduras.http.request.UserCreateRequest;
import com.invest.honduras.http.request.UserPasswordUpdateRequest;
import com.invest.honduras.http.request.UserStateUpdateRequest;
import com.invest.honduras.http.request.UserUpdateBlockchainRequest;
import com.invest.honduras.http.request.UserUpdateRequest;
import com.invest.honduras.http.response.UserDidDTO;
import com.invest.honduras.http.response.UserItemDTO;
import com.invest.honduras.http.response.UserProfileDTO;
import com.invest.honduras.http.response.UserTokenDTO;
import com.invest.honduras.service.NotifyService;
import com.invest.honduras.service.UserService;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	UserDao userDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	StatusDao statusDao;

	@Autowired
	NotifyService notifyService;

	@Autowired
	BlockChainClient blockChainClient;

	@Autowired
	ApplicationBlockChain app;

	@Autowired
	ConfigurationProject config;

	public List<Role> listRole() {

		return roleDao.listRole();
	}

	public List<StatusUser> listStatus() {

		return statusDao.listStatus();
	}

	@Override
	public Mono<List<UserItemDTO>> findAllUser() {
		final List<Role> roles = listRole();
		final List<StatusUser> listStatus = listStatus();

		final UserMap userMap = new UserMap();

		return userDao.listAllUser().collectList().map(list -> {

			List<UserItemDTO> listUserItemResponse = new ArrayList<>();

			list.forEach(user -> {
				UserItemDTO destiny = new UserItemDTO();
				userMap.mapping(user, destiny);
				userMap.completeRole(destiny, roles);
				userMap.completeStatus(destiny, listStatus);
				listUserItemResponse.add(destiny);
			});

			return listUserItemResponse;
		});
	}

	@Override
	public Mono<List<String>> findCompany() {
		return userDao.listAllUser().collectList().map(list -> {

			List<String> company = new ArrayList<>();

			list.forEach(user -> {
				if (!Strings.isEmpty(user.getCompany()))
					company.add(user.getCompany().trim());
			});

			List<String> listWithoutDuplicates = company.stream().distinct().collect(Collectors.toList());

			return listWithoutDuplicates;
		});
	}

	@Override
	public Mono<UserItemDTO> createUser(UserCreateRequest userRequest, String token)  {

		List<Role> roles = listRole();
		List<StatusUser> listStatus = listStatus();
		String username = Util.decodeToken(token);

		UserLogin userGenericLogin = new UserLogin();
		userGenericLogin.setUserLogin(username);
		UserMap userMap = new UserMap();

		return userDao.findByEmail(username).flatMap(userAdmin -> {
			log.info("********** validateRoleAdmi ******* ");
			
			validateRoleAdmin(userAdmin.getProxyAddress());
			
			return userDao.createUser(userRequest, userGenericLogin, listStatus, roles).flatMap(user -> {

				log.info("********** addBlockChain ******* ");
				addBlockChain(userAdmin.getProxyAddress(), userGenericLogin, user);
				log.info("********** sendEmailCreateUser ******* ");
				UserItemDTO destiny = new UserItemDTO();
				userMap.mapping(user, destiny);

				userMap.completeRole(destiny, roles);
				userMap.completeStatus(destiny, listStatus);

				sendEmailCreateUser(user, roles);

				return Mono.just(destiny);

			});
		});

	}

	private void sendEmailCreateUser(User user, List<Role> roles) {

		List<String> notifyRolRequest = new ArrayList<String>();

		ItemUserNotifyRequest userToNotifyRequest = new ItemUserNotifyRequest();
		userToNotifyRequest.setName(user.getFullname());
		userToNotifyRequest.setTypeNotify(Constant.TYPE_INVITATION_USER);
		userToNotifyRequest.setEmail(user.getEmail());
		userToNotifyRequest.setUserId(user.getId());

		// roles x notificate
		roles.forEach(role -> {
			user.getRoles().forEach(request -> {
				if (role.getCode().equals(request.getCode())) {
					notifyRolRequest.add(role.getName());
					userToNotifyRequest.setRoles(notifyRolRequest);
				}

			});
		});

		try {
			notifyService.sendUserNotify(userToNotifyRequest);
		} catch (Exception e) {
			log.error("Error.sendInvitationNotify ", e);
		}
	}

	private void addBlockChain(String proxyAdministrator, UserLogin userLogin, final User user) {

		UserBlockchainRequest request = new UserBlockchainRequest();
		request.setEmailUser(user.getEmail());
		request.setProxyAddressAdministrator(proxyAdministrator);
		blockChainClient.addUser(request);

	}

	private void updateBlockChain(String proxyAdministrator, UserLogin userLogin, User user) {

		UserUpdateBlockchainRequest request = new UserUpdateBlockchainRequest();
		request.setEmailUser(user.getEmail());
		request.setProxyAddressAdministrator(proxyAdministrator);
		request.setOldRole(user.getRolesOld());
		blockChainClient.updateUser(request);

	}

	@Override
	public Mono<UserItemDTO> findByIdUser(String id) {
		List<Role> roles = listRole();
		List<StatusUser> listStatus = listStatus();
		UserMap userMap = new UserMap();

		return userDao.findByIdUser(id).map(user -> {
			UserItemDTO destiny = new UserItemDTO();

			userMap.mapping(user, destiny);

			userMap.completeRole(destiny, roles);
			userMap.completeStatus(destiny, listStatus);
			return destiny;
		});
	}

	@Override
	public Mono<UserTokenDTO> getToken(String email) {
		UserTokenDTO userTokenDTO = new UserTokenDTO();
		userTokenDTO.setToken(UUID.randomUUID().toString());
		SessionTokenJWT.getInstance().put(userTokenDTO.getToken(), email);

		return Mono.just(userTokenDTO);
	}

	@Override
	public Mono<UserItemDTO> updateUser(String id, UserUpdateRequest userUpdateRequest, String token) {
		List<Role> roles = listRole();
		List<StatusUser> listStatus = listStatus();

		String username = Util.decodeToken(token);
		UserLogin userLogin = new UserLogin();
		userLogin.setUserLogin(username);

		UserMap userMap = new UserMap();

		return userDao.findByEmail(username).flatMap(userAdmin -> {
			validateRoleAdmin(userAdmin.getProxyAddress());
			return userDao.updateUser(id, userUpdateRequest, userLogin, listStatus()).flatMap(user -> {
				updateBlockChain(userAdmin.getProxyAddress(), userLogin, user);
				UserItemDTO destiny = new UserItemDTO();
				userMap.mapping(user, destiny);
				userMap.completeRole(destiny, roles);
				userMap.completeStatus(destiny, listStatus);

				return Mono.just(destiny);
			});
		});
	}

	@Override
	public Mono<User> findByEmail(String email) {
		List<Role> roles = listRole();

		return userDao.findByEmail(email).flatMap(user -> {

			if (user.getCodeStatus().equals(Constant.USER_CODE_STATUS_ENABLED)) {
				List<String> notifyRolRequest = new ArrayList<String>();
				ItemUserNotifyRequest userToNotifyRequest = new ItemUserNotifyRequest();
				userToNotifyRequest.setEmail(user.getEmail());
				userToNotifyRequest.setUserId(user.getId());

				// roles x notificate
				roles.forEach(role -> {
					user.getRoles().forEach(request -> {
						if (role.getCode().equals(request.getCode())) {
							notifyRolRequest.add(role.getName());
							userToNotifyRequest.setRoles(notifyRolRequest);
						}
					});
				});

				userToNotifyRequest.setTypeNotify(Constant.TYPE_FORGET_PASSWORD);

				try {
					notifyService.sendUserNotify(userToNotifyRequest);
				} catch (Exception e) {
					log.error("Error.sendInvitationNotify ", e);
				}
			}

			return Mono.just(user);
		});
	}

	@Override
	public Mono<User> updatePasswordUser(String id, UserPasswordUpdateRequest userRequest, String token) {

		String username = Util.decodeToken(token);
		UserLogin userLogin = new UserLogin();
		userLogin.setUserLogin(username);

		return userDao.updatePasswordUser(id, userRequest, userLogin, listStatus()).flatMap(user -> {
			sendToMail(userRequest.getType(), user);
			return Mono.just(user);
		});
	}

	@Override
	public Mono<User> updateStateUser(String id, UserStateUpdateRequest userStateUpdateRequest, String token) {
		String username = Util.decodeToken(token);

		UserLogin userLogin = new UserLogin();
		userLogin.setUserLogin(username);
		return userDao.updateStateUser(id, userStateUpdateRequest, userLogin, listStatus()).flatMap(user -> {

			return Mono.just(user);
		});
	}

	@Override
	public Mono<UserProfileDTO> getProfileUser(String id) {
		List<Role> roles = listRole();
		UserMap userMap = new UserMap();

		return userDao.findByIdUser(id).map(user -> {
			UserProfileDTO destiny = new UserProfileDTO();

			userMap.mappingProfile(user, destiny);
			userMap.completeRoleProfile(destiny, roles);

			return destiny;
		});
	}

	public void sendToMail(String type, User user) {

		try {
			if (type.equals(Constant.TYPE_CHANGE_PASSWORD)) {
				///// update password
				List<Role> roles = listRole();

				ItemUserNotifyRequest userToNotifyRequest = new ItemUserNotifyRequest();
				userToNotifyRequest.setEmail(user.getEmail());
				userToNotifyRequest.setUserId(user.getId());
				userToNotifyRequest.setTypeNotify(Constant.TYPE_CHANGE_PASSWORD);
				List<String> notifyRolRequest = new ArrayList<String>();

				// roles x notificate
				roles.forEach(role -> {
					user.getRoles().forEach(request -> {
						if (role.getCode().equals(request.getCode())) {
							notifyRolRequest.add(role.getName());
							userToNotifyRequest.setRoles(notifyRolRequest);
						}

					});
				});

				notifyService.sendUserNotify(userToNotifyRequest);
			} else {

				///// generate first complete password
				UserNotifyRequest userNotifyRequest = new UserNotifyRequest();

				userNotifyRequest.setEmail(user.getEmail());
				userNotifyRequest.setFullname(user.getFullname());
				userNotifyRequest.setUserRol(findUserByRol(Constant.CODE_ROLE_ADMIN));
				notifyService.sendUpdateUserNotify(userNotifyRequest);
			}

		} catch (Exception e) {
			log.error("Error.sendToMail ", e);
		}

	}

	public List<ItemUserNotifyRequest> findUserByRol(String role) {
		List<ItemUserNotifyRequest> listUserInvitation = new ArrayList<ItemUserNotifyRequest>();
		Flux<User> userByRole = userDao.findUserByRol(role);

		userByRole.collectList().block().forEach(user -> {
			log.info("admin  user :" + user);
			ItemUserNotifyRequest userToNotifyRequest = new ItemUserNotifyRequest();
			userToNotifyRequest.setUserId(user.getId());
			userToNotifyRequest.setEmail(user.getEmail());
			userToNotifyRequest.setName(user.getFullname());
			userToNotifyRequest.setTypeNotify(Constant.TYPE_COMPLETE_PASSWORD);
			listUserInvitation.add(userToNotifyRequest);
		});
		return listUserInvitation;

	}

	@Override
	public Mono<UserDidDTO> updateDidUser(String email, JwtPayload jwtPayload) {
		log.info("UserServiceImpl.updateDidUser.email:{}", email);

		return userDao.findByEmail(email).flatMap(user -> {
			UserLogin userLogin = new UserLogin();
			userLogin.setUserLogin(user.getEmail());
		

				try {
					if (!jwtPayload.getSub().toUpperCase()
							.equals(Secp256k1Utils.publicKeyToAddress(jwtPayload.getIss()).toUpperCase())) {
						log.info("UserServiceImpl.error.DIFRENTE_SUB_ISS");
						throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_DID_VALIDATE_PUBLIC.name());
					}
				} catch (Exception e) {
					throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
				}

				if (!jwtPayload.getAud().equals(config.getConfiguration().getDidBackend())) {

					log.info("UserServiceImpl.error.USER_DID_AUD");

					throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.USER_DID_AUD.name());
				}

			
				return setCap(user.getProxyAddress(), jwtPayload.getSub()).flatMap(flag -> {

					return userDao
							.updateDidUser(
									user.getId(), MnidUtils.DID + MnidUtils
											.encode(config.getConfiguration().getNetworkId(), user.getProxyAddress()),
									userLogin)
							.flatMap(i -> {

								sendEmail(user);
								UserDidDTO userDidDto = new UserDidDTO();

								userDidDto.setDid(MnidUtils.DID + MnidUtils
										.encode(config.getConfiguration().getNetworkId(), i.getProxyAddress()));

								log.info("did:{}", userDidDto.getDid());
								return Mono.just(userDidDto);
							});
				});

			
		});
	}

	private Mono<Boolean> setCap(String proxyAddressUser, String device) {
		UserCapRequest request = new UserCapRequest();
		request.setProxyAddressUser(proxyAddressUser);
		request.setAddressDevice(device);
		return blockChainClient.setCap(request).map(response -> {
			if (HttpStatus.OK.value() == response.getStatus()) {

				return true;
			} else {
				throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
			}

		});
	}

	public void sendEmail(User user) {

		log.info("enviando email" + user.getEmail());
		ItemUserNotifyRequest request = new ItemUserNotifyRequest();
		request.setEmail(user.getEmail());
		request.setUserId(user.getId());
		request.setName(user.getFullname());
		request.setObs(Constant.NOTIFY_DID_USER_ATTACH);
		request.setTemplate(Constant.TYPE_TEMPLATE_GENERAL);
		request.setSubject(Constant.NOTIFY_SUBJET);
		request.setTypeNotify(Constant.TYPE_NOTFIY_GENERAL);
		request.setDetailsURL(config.getConfiguration().getUrlLogin());

		notifyService.sendUserNotify(request);

	}

	private void validateRoleAdmin(String proxyIdentity) {
						
		try {
			log.info("validateRoleAdmin :" + proxyIdentity);			
			if (StringUtils.isEmpty(proxyIdentity)) {
				throw new GlobalException(HttpStatus.BAD_REQUEST, 
						EnumMessage.MESSAGE_USER_NOT_HAVE_IDENTITY.name());
			}
			if (!app.getIRoles().hasRoleUser(Constant.CODE_ROLE_ADMIN, proxyIdentity)) {
				throw new GlobalException(HttpStatus.BAD_REQUEST,
						EnumMessage.USER_NOT_ROLE_IN_BLOCKCHAIN.name() + ":"+ Constant.CODE_ROLE_ADMIN);
			}
		} catch (SocketTimeoutException e) {
			
			log.info("TimeoutException ");			
			throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, "Timeout Node Ethcore");
					
		} catch (Exception e) {
			
			throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR,  e.getMessage());
		}		
	}

}
