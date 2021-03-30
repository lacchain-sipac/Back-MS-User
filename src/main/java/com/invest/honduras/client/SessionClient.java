package com.invest.honduras.client;

public interface SessionClient {
 

    boolean closeSession(String key);
    boolean existSession(String key);
    
}
