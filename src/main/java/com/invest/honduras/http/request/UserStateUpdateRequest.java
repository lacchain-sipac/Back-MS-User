package com.invest.honduras.http.request;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStateUpdateRequest {

	@ApiModelProperty(notes = "estado del usuario" , example = "H")
	@NotNull
	private String codeStatus;
}
