package com.invest.honduras.http.response;

import java.util.List;

import com.invest.honduras.http.HttpResponse;

public class UserFindAllResponse extends HttpResponse<List<UserItemDTO>> {

	public UserFindAllResponse(int status, List<UserItemDTO> userItems) {
		super(status, userItems);
	}

}
