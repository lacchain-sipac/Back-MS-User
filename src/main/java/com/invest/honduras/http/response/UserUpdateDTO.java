package com.invest.honduras.http.response;

import java.util.List;

import com.invest.honduras.domain.model.Role;
import com.invest.honduras.domain.model.StatusUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

	private String surnames;
	private String fullname;
	private String email;
	private List<Role> roles;
	private StatusUser status;
	private String company;
}
