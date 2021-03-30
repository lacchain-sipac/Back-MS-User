package com.invest.honduras.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "status_user")
public class StatusUser {

	@ApiModelProperty(notes = "codigo del estado de usuario" , example ="P")
	private String code;
	
	@ApiModelProperty(notes = "nombre del estado de usuario" , example ="Por Completar")
	private String name;
}
