package com.invest.honduras.http.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserForgetPasswordRequest {

	@ApiModelProperty(notes = "email del usuario" , example = "email@email.com")
	private String email;
}
