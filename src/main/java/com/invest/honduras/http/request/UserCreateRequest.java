package com.invest.honduras.http.request;


import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest  {

	@ApiModelProperty(notes = "email de usuario" , example = "mail@email.com")
	@NotNull
	private String email;
	
	@ApiModelProperty(notes = "lista de roles asociados al usuario")
	@NotNull
	private List<RoleRequest> roles;
	
	@ApiModelProperty(notes = "nombre completo del usuario", example ="Renato Navarrete Guerrero")
	@NotNull
	private String fullname;
	
	@ApiModelProperty(notes = "nombre corto del usuario" , example ="Navarrete Guerrero")
	@NotNull
	private String surnames;

	@ApiModelProperty(notes = "compañia asociada al usuario",example = "Everis SAC")
	private String company;
}
