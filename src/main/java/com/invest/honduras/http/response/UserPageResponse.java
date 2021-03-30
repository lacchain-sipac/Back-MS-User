package com.invest.honduras.http.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPageResponse {


	public UserPageResponse(List<UserItemDTO> userItems, long totalPages, long totalElements) {
		this.userItems = userItems;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
	}
	
	long totalPages;


	long totalElements;
	
	
	List<UserItemDTO> userItems;
}
