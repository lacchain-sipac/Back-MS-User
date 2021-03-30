package com.invest.honduras.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.ConfigurationDao;
import com.invest.honduras.domain.model.Configuration;

import lombok.Getter;

@Component
@Getter
public class ConfigurationProject {

	private Configuration configuration;
	
	@Autowired
	ConfigurationDao configurationDao;
	
	@Bean
	public void getRuleProject() {

		 configuration = configurationDao.find() ;
			
		
	}
}
