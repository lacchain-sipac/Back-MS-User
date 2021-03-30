package com.invest.honduras.domain.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "role")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role {

	@ApiModelProperty(notes = "codigo del rol" , example ="ROLE_COO_TEC")
	private String code;
	
	@ApiModelProperty(notes = "nombre del rol" , example ="Coordinador Tecnico")
	private String name;
	
	@ApiModelProperty(notes = "descripcion del rol" , example ="Coordinador Tecnico")
	private String description;
	
	@ApiModelProperty(notes = "provilegios del rol")
	private List<Privilege> privileges;
}
