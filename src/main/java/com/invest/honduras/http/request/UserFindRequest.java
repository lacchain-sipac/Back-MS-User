package com.invest.honduras.http.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFindRequest  {

	private String search;
	
	private Long pageSize;
	
	private Long pageNumber;
	
	private String sortField;

}
