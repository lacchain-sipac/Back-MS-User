package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.invest.honduras.domain.model.StatusUser;

public interface StatusRepository extends MongoRepository<StatusUser, String> {

	
}