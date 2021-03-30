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
public class MnidResponse implements Serializable
{
	private static final long serialVersionUID = 4657620395519981451L;
	private String mnid;
	
}
