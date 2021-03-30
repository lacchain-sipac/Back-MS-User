package com.invest.honduras.http.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordUpdateJWTRequest {
	
	@NotNull
	private String jwt;

}
