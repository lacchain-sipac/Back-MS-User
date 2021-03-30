package com.invest.honduras.http.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDidDTO {
	@ApiModelProperty(notes = "did de usuario", example = "did:ev:124567")
	private String did;

}
