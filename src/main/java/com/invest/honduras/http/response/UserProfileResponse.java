package com.invest.honduras.http.response;

import com.invest.honduras.http.HttpResponse;

public class UserProfileResponse extends HttpResponse<UserProfileDTO> {

	
	public UserProfileResponse(int status, UserProfileDTO data ) {
		super(status, data );
	}

}
