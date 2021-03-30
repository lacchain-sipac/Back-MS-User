package com.invest.honduras.http.response;

import com.invest.honduras.http.HttpResponse;

public class UserTokenResponse extends HttpResponse<UserTokenDTO> {

	
	public UserTokenResponse(int status, UserTokenDTO data) {
		super(status, data );
	}

}
