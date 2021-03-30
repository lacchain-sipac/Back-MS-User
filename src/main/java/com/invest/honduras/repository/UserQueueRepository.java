package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.invest.honduras.domain.model.UserQueue;

public interface UserQueueRepository extends MongoRepository<UserQueue, String> {
	
}
