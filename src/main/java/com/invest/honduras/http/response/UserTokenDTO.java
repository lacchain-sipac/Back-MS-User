package com.invest.honduras.http.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserTokenDTO {
	
	@ApiModelProperty(notes = "token", example = "1234-1234-1234-1234")
	private String token;

}
