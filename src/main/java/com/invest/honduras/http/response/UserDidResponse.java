package com.invest.honduras.http.response;

import com.invest.honduras.http.HttpResponse;

public class UserDidResponse extends HttpResponse<UserDidDTO> {

	
	public UserDidResponse(int status, UserDidDTO data ) {
		super(status, data );
	}

}
