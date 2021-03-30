package com.invest.honduras.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleQueue  {
	private String id;
	private String proxyUser;
	private String proxyAddressAdministrator;
	private String role;
	private boolean insert;
}
