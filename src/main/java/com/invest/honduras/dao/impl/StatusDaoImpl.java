package com.invest.honduras.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.StatusDao;
import com.invest.honduras.domain.model.StatusUser;
import com.invest.honduras.repository.StatusRepository;

@Component
public class StatusDaoImpl implements StatusDao {

	@Autowired
	StatusRepository statusRepository;

	@Override
	public List<StatusUser> listStatus() {
		return statusRepository.findAll();
	}

}
