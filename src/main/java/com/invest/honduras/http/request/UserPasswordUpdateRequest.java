package com.invest.honduras.http.request;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordUpdateRequest {

	@ApiModelProperty(notes = "tipo de accion para actualizar el password", example = "COMPLETE-PASSWORD  o CHANGE-PASSWORD")
	@NotNull
	private String type; // COMPLETE-PASSWORD - CHANGE-PASSWORD
	
	
	@ApiModelProperty(notes = "password del usuario", example = "Peru12345678@123")
	@NotNull
	private String password;
	
	@ApiModelProperty(notes = "confirmacion de password del usuario", example = "Peru12345678@123")
	@NotNull
	private String confirmPassword;
	
	@ApiModelProperty(notes = "codigo de verificacion", example = "123456789")
	@NotNull
	private String verifiedCode;
	
	@ApiModelProperty(notes = "secreto", example = "secret")
	@NotNull
	private String secretKey;
	
	@ApiModelProperty(notes = "flag de doble autenticacion", example = "false")
	@NotNull
	private boolean es2FA;
}
