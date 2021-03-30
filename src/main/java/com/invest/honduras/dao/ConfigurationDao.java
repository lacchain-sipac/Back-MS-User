package com.invest.honduras.dao;

import com.invest.honduras.domain.model.Configuration;

import reactor.core.publisher.Mono;

public interface ConfigurationDao {

	Configuration find();	
}
