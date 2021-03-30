package com.invest.honduras.http.response;

import java.util.List;

import com.invest.honduras.http.HttpResponse;

public class CompanyFindResponse extends HttpResponse<List<String>> {

	public CompanyFindResponse(int status, List<String> list) {
		super(status, list);
	}

}
