package com.invest.honduras.domain.model;

import java.util.List;

import com.invest.honduras.http.request.RoleRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserQueue  {
	private String id;
	private String emailUser;
	private String proxyAddressAdministrator;
	private List<RoleRequest> roles;

}
