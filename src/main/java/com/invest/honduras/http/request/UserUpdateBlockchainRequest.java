package com.invest.honduras.http.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateBlockchainRequest {
	private String proxyAddressAdministrator;
	private String emailUser;

	private List<RoleRequest> oldRole;
}
