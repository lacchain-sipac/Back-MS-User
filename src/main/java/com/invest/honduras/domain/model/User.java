package com.invest.honduras.domain.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.invest.honduras.http.request.RoleRequest;
import com.invest.honduras.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "user")
public class User extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	private String surnames;
	private String fullname;
	private String email;
	private List<RoleRequest> roles;
	private String password;	
	private String codeStatus;
	private String proxyAddress;
	private String company;
	private String did;
	private boolean completed;
	private String secretkey;
	private List<RoleRequest> rolesOld;

}
