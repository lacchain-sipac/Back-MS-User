package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.invest.honduras.domain.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

	
}