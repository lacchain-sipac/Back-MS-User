package com.invest.honduras.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseGeneral {

	 private int status;
	 private ResponseItem data;
	 
	 private String error;

}