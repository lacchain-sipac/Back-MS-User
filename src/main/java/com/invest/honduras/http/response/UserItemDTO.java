package com.invest.honduras.http.response;

import java.util.Date;
import java.util.List;

import com.invest.honduras.domain.model.Role;
import com.invest.honduras.domain.model.StatusUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserItemDTO {
	
	@ApiModelProperty(notes = "codigo de usuario" , example = "123456789")	
	private String id;
	
	@ApiModelProperty(notes = "nombre completo del usuario", example ="Renato Navarrete Guerrero")
	
	private String fullname;
	@ApiModelProperty(notes = "nombre corto del usuario" , example ="Navarrete Guerrero")
	private String surnames;
	
	@ApiModelProperty(notes = "email del usuario" , example = "mail@email.com")
	private String email;
	
	@ApiModelProperty(notes = "estado completado de usuario" , example = "true")	
	private boolean completed;
	
	@ApiModelProperty(notes = "lista de roles asociados a un usuario")
	private List<Role> roles;
	
	@ApiModelProperty(notes = "compañia asociado al usuario", example = "Everis SAC")	
	private String company;
	
	@ApiModelProperty(notes = "estado del usuario")
	private StatusUser status;
	
	@ApiModelProperty(notes = "Autor de la creacion del registro" , example = "donald@correo.com")	
	private String createdBy;
	
	@ApiModelProperty(notes = "fecha de creacion" ,example ="2020-08-12T17:11:00.907+000")
	private Date createdDate;
	
	@ApiModelProperty(notes = "Autor de la ultima modificacion", example = "renato@correo.com")
	private String lastModifiedBy;
	
	@ApiModelProperty(notes = "Ultima modificacion" , example ="2020-09-12T17:11:00.907+000")
	private Date lastModifiedDate;

}
