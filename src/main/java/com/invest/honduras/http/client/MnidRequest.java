package com.invest.honduras.http.client;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MnidRequest implements Serializable
{
	private static final long serialVersionUID = 5783012465072243825L;
	
	private String network;
	private String address;
	
}
