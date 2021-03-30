package com.invest.honduras.http.response;

import java.util.List;

import com.invest.honduras.domain.model.Role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
	
	@ApiModelProperty(notes = "codigo de usuario" , example = "123456789")
	private String id;
	
	@ApiModelProperty(notes = "nombre completo del usuario", example ="Renato Navarrete Guerrero")
	private String fullname;
	
	@ApiModelProperty(notes = "nombre corto del usuario" , example ="Navarrete Guerrero")
	private String surnames;
	
	@ApiModelProperty(notes = "email del usuario" , example = "mail@email.com")
	private String email;
	
	/* ADD RDELAROSA 12/06/2020 */
	
	@ApiModelProperty(notes = "compañia asociado al usuario", example = "Everis SAC")
	private String company;
	
	@ApiModelProperty(notes = "lista de roles asociados a un usuario")
	private List<Role> roles;
}
