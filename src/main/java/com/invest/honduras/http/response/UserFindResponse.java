package com.invest.honduras.http.response;

import com.invest.honduras.http.HttpResponse;

public class UserFindResponse extends HttpResponse<UserPageResponse> {

	public UserFindResponse(int status, UserPageResponse data) {
		super(status, data);
	}

}
