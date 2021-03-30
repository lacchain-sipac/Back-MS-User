package com.invest.honduras.domain.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.invest.honduras.domain.model.Role;
import com.invest.honduras.domain.model.StatusUser;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserLogin;
import com.invest.honduras.http.request.UserCreateRequest;
import com.invest.honduras.http.request.UserPasswordUpdateRequest;
import com.invest.honduras.http.request.UserStateUpdateRequest;
import com.invest.honduras.http.request.UserUpdateRequest;
import com.invest.honduras.http.response.UserItemDTO;
import com.invest.honduras.http.response.UserProfileDTO;
import com.invest.honduras.http.response.UserUpdateDTO;
import com.invest.honduras.util.Constant;

public class UserMap {

	/* Update User
	**/
	public void mapping(UserUpdateRequest source, User destiny, UserLogin userLogin, List<StatusUser> status) {

		destiny.setSurnames(source.getSurnames().trim());
		destiny.setFullname(source.getFullname().trim());
		// añadir restriccion
		destiny.setRoles(source.getRoles());
		destiny.setCompany(source.getCompany());
		destiny.setCodeStatus(source.getStatus());
		destiny.setLastModifiedBy(userLogin.getUserLogin());
		destiny.setLastModifiedDate(new Date());
	}



	public void mappingPassword(UserPasswordUpdateRequest source, User destiny, UserLogin userLogin,
			List<StatusUser> status) {
 
		destiny.setPassword(source.getPassword());
		destiny.setLastModifiedBy(userLogin.getUserLogin());
		if (Constant.TYPE_COMPLETE_PASSWORD.equals(source.getType())) {			
			destiny.setCodeStatus(Constant.USER_HABILITADO);
			destiny.setCompleted(true);
		}
		if(source.isEs2FA()){
			destiny.setSecretkey(source.getSecretKey());
		}
		
		destiny.setLastModifiedDate(new Date());
	}

	public void mappingUpdateDid(String did, User destiny, UserLogin userLogin) {

		destiny.setDid(did);
		destiny.setLastModifiedBy(userLogin.getUserLogin());
	
		destiny.setLastModifiedDate(new Date());
	}
	/**
	 * Mapping CreateUser
	**/
	public void mapping(UserCreateRequest source, User destiny, UserLogin userLogin, List<StatusUser> status,
			List<Role> roles) {

		destiny.setCompany(source.getCompany());
		destiny.setFullname(source.getFullname().trim());
		destiny.setSurnames(source.getSurnames().trim());
		destiny.setEmail(source.getEmail().toLowerCase());
		destiny.setCodeStatus(Constant.USER_POR_COMPLETAR);
		destiny.setRoles(source.getRoles());		
		destiny.setCreatedBy(userLogin.getUserLogin());
		destiny.setCreatedDate(new Date());
		destiny.setLastModifiedBy(userLogin.getUserLogin());
		destiny.setLastModifiedDate(destiny.getCreatedDate());
	}

	/**
	 * response UserItemResponse (response) in updatecreateUser service
	 ***/	
	
	public void mappingProfile(User source, UserProfileDTO destiny) {
		
		destiny.setEmail(source.getEmail().toLowerCase());
		destiny.setId(source.getId());
		destiny.setSurnames(source.getSurnames().trim());
		destiny.setFullname(source.getFullname().trim());
		
		/* ADD RDELAROSA 12/06/2020 */
		//if(source.getCompany()!=null) {
			//destiny.setCompany(source.getCompany().trim());
			destiny.setCompany(source.getCompany());
		//}
		
		destiny.setRoles(new ArrayList<>());
		
		source.getRoles().forEach(roleSource -> {
			Role roleDestinity = new Role();
			////cambiar 
//			List<Privilege> listPrivilege = new ArrayList<Privilege>();
			
			roleDestinity.setCode(roleSource.getCode());
//			if (roleSource.getPrivileges() != null) {
//				roleSource.getPrivileges().forEach(codePage -> {
//					Privilege privilege = new Privilege();
//					privilege.setCode(codePage);
//					listPrivilege.add(privilege);
//				});
//				roleDestinity.setPrivileges(listPrivilege);
//			}
			destiny.getRoles().add(roleDestinity);

		});

		
	}
	public void mapping(User source, UserItemDTO destiny) {

		destiny.setEmail(source.getEmail().toLowerCase());
		destiny.setId(source.getId());
		destiny.setStatus(new StatusUser());
		destiny.getStatus().setCode(source.getCodeStatus());
		destiny.setSurnames(source.getSurnames().trim());
		destiny.setFullname(source.getFullname().trim());		
		destiny.setCompleted((source.isCompleted()));
		destiny.setCreatedBy(source.getCreatedBy());
		destiny.setCreatedDate(source.getCreatedDate());
		destiny.setLastModifiedBy(source.getLastModifiedBy());
		destiny.setLastModifiedDate(source.getLastModifiedDate());
		destiny.setCompany(source.getCompany());
		destiny.setRoles(new ArrayList<>());
		
		source.getRoles().forEach(roleSource -> {
			Role roleDestinity = new Role();
			////cambiar 
//			List<Privilege> listPrivilege = new ArrayList<Privilege>();
			
			roleDestinity.setCode(roleSource.getCode());
//			if (roleSource.getPrivileges() != null) {
//				roleSource.getPrivileges().forEach(codePage -> {
//					Privilege privilege = new Privilege();
//					privilege.setCode(codePage);
//					listPrivilege.add(privilege);
//				});
//				roleDestinity.setPrivileges(listPrivilege);
//			}
			destiny.getRoles().add(roleDestinity);

		});
	}

	 

	public void mappingUpdate(User source, UserUpdateDTO destiny) {

		destiny.setEmail(source.getEmail().toLowerCase());
		destiny.setStatus(new StatusUser());
		destiny.getStatus().setCode(source.getCodeStatus());
		destiny.setCompany(source.getCompany());
		destiny.setSurnames(source.getSurnames().trim());
		destiny.setFullname(source.getFullname().trim());
		
		source.getRoles().forEach(roleSource -> {

			destiny.setRoles(new ArrayList<Role>());
			Role roleDestinity = new Role();
			roleDestinity.setCode(roleSource.getCode());
			destiny.getRoles().add(roleDestinity);

		});

	}

	public void completeRole(UserItemDTO user, List<Role> roles) {
		if (user.getRoles() != null)
			user.getRoles().forEach(userRole -> {
				roles.forEach(role -> {
					
					if (role.getCode().equals(userRole.getCode())) {
												
						userRole.setName(role.getName());
						userRole.setDescription(role.getDescription());
						if (userRole.getPrivileges() != null) {
							userRole.getPrivileges().forEach(section -> {
								if (role.getPrivileges() != null) {
									role.getPrivileges().forEach(roleSection -> {
										if (section.getCode().equals(roleSection.getCode())) {
											section.setDescription(roleSection.getDescription());
										}
									});

								}
							});
						}
					}

				});
			});

	}

	public void completeRoleProfile(UserProfileDTO user, List<Role> roles) {
		if (user.getRoles() != null)
			user.getRoles().forEach(userRole -> {
				roles.forEach(role -> {
					
					if (role.getCode().equals(userRole.getCode())) {
												
						userRole.setName(role.getName());
						userRole.setDescription(role.getDescription());
						if (userRole.getPrivileges() != null) {
							userRole.getPrivileges().forEach(section -> {
								if (role.getPrivileges() != null) {
									role.getPrivileges().forEach(roleSection -> {
										if (section.getCode().equals(roleSection.getCode())) {
											section.setDescription(roleSection.getDescription());
										}
									});

								}
							});
						}
					}

				});
			});

	}	
	public void mapping(UserStateUpdateRequest source, User destiny, UserLogin userLogin, List<StatusUser> status) {

		destiny.setCodeStatus(source.getCodeStatus());
		destiny.setLastModifiedBy(userLogin.getUserLogin());
		destiny.setLastModifiedDate(new Date());
	}


	
	public void completeStatus(UserItemDTO userItemResponse, List<StatusUser> listStatus) {
		if (userItemResponse.getStatus() != null)
			listStatus.forEach(status -> {
				if (status.getCode().equals(userItemResponse.getStatus().getCode()))
					userItemResponse.setStatus(status);

			});
	}


}
