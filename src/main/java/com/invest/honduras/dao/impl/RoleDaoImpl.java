package com.invest.honduras.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.RoleDao;
import com.invest.honduras.domain.model.Role;
import com.invest.honduras.repository.RoleRepository;

@Component
public class RoleDaoImpl implements RoleDao {

	//Role
	@Autowired
	RoleRepository roleRepository;

	@Override
	public List<Role> listRole() {
		return roleRepository.findAll();
	}

}
