package com.invest.honduras.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "queue_tx_fail")
public class QueueTransactionFail {
	@Id
	private String id;
	private UserRoleQueue userRole;
	private UserRoleProjectQueue userRoleProject;
	private UserQueue user;
	private String revertReason;
}
