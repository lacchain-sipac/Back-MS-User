package com.invest.honduras.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Privilege {

	@ApiModelProperty(notes = "descripcion del privilegio" , example ="Privilegio 1")
	private String description;
	
	@ApiModelProperty(notes = "codigo del privilegio" , example ="PRI_1")
	private String code;
}
