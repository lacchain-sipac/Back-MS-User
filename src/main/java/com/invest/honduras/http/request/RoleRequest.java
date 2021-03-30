package com.invest.honduras.http.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoleRequest {

	@ApiModelProperty(notes = "codigo del rol", example ="ROLE_1")
	private String code;
}
