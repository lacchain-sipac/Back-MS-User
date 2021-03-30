package com.invest.honduras.config;

import java.util.HashMap;
import java.util.Map;

public class SessionTokenJWT {

	private Map<String, String> listSessionJwt = new HashMap<>();
	
    private static SessionTokenJWT INSTANCE; 
    
    private SessionTokenJWT() {        
    }
     
    public static SessionTokenJWT getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SessionTokenJWT();
        }
         
        return INSTANCE;
    }
    
    public String getValue(String key) {
    	String value = listSessionJwt.get(key);
    	listSessionJwt.remove(key);
    	return value;
    }
    
    public void put(String key, String value) {
    	
    	listSessionJwt.values().removeIf(obj -> obj.equals(value));

    	listSessionJwt.put(key, value);
    	
    }
}
