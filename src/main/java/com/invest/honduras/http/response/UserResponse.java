package com.invest.honduras.http.response;

import com.invest.honduras.http.HttpResponse;

public class UserResponse extends HttpResponse<UserItemDTO> {

	
	public UserResponse(int status, UserItemDTO data ) {
		super(status, data);
	}

}
