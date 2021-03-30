package com.invest.honduras.http.client;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UserNotifyRequest {

	private String email;
	private String fullname;		
	private List<ItemUserNotifyRequest> userRol;
	
	
}