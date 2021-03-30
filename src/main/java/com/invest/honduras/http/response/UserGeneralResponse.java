package com.invest.honduras.http.response;

import com.invest.honduras.http.HttpResponse;

public class UserGeneralResponse extends HttpResponse<String> {

	public UserGeneralResponse(int status, String data) {
		super(status, data );
	}

}
